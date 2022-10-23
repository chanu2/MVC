package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
//            Member member = new Member();
//            member.setUsername("이훈일");
//            member.setAge(30);
//            em.persist(member);
//
//            em.flush();
//            em.persist(member);


            /*
            // 첫번쨰 방법 Query
            List resultList = em.createQuery("select m.username , m.age from Member m").getResultList();  //타입이 정해지지 않은 것

            // 타입이 정해지지 않기 때문에 object 타입이다

            Object o = resultList.get(0);
            Object[] result = (Object[]) o;
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

             */
            /* 두번째 방법 타입이 정해지지 않을 때
            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username , m.age) from Member m",
                    MemberDTO.class).getResultList(); // 엔티티가 아닌  MemberDTO 이런 경우로 갈때는 항상 new operation으로 해줘야 한다
            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());
           */


            //파라미터 바인딩 - 이름 기준
            /*
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "이훈일") //이름 기준
                    .getSingleResult();
            System.out.println("result= " + result.getUsername());

             */


            
/*
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();  // 컬렉션이 반환이 될거다  결과가 하나 이상일 때

            Member result = query1.getSingleResult();  // 결과가 정확히 하나여야 한다 아니면 무조건 오류 발생
            System.out.println("result = " + result);

 */

//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query3 = em.createQuery("select m.username, m.age from Member m");  // String 과 int 같이 있어서 타입을 정확히 알 수 없다


            /*  원하는 만큼을 db에서  가져오는 코드
            for(int i=0;i<100;i++){
                Member member = new Member();
                member.setUsername("member"+i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)  // 내림차순으로 정렬된다
                    .setFirstResult(1)  // 어디서 시작하고
                    .setMaxResults(10)  // 어디서 끝나는지
                    .getResultList();
            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

             */


            //내부 조인, 외부조인

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원2");
            member3.setTeam(teamB);
            em.persist(member3);



            em.flush();
            em.clear();

            // 벌크연산 -->영속성 컨텍스트 를 무시하고 데이터 베이스에 직접 쿼리
            // 플러쉬 자동호춣 commit or query 날릴때 자동
            int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();  // 모두 20살로 바꾸기  //db에만 반영된다 영속성 컨텍스트에는 x

            System.out.println("resultCount = " + resultCount);

            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("findMember .getAge() = " + findMember.getAge());  // 0 이 나와버린다 영속성 컨텍스트에 있는 값을 가져오기 때문에 그래서 clear가 필요



            /*
            //named쿼리
            List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "회원1").getResultList();

            for (Member member : result) {
                System.out.println("member = " + member);
            }

             */




            //List<Member> result = em.createQuery("select m from Member m inner join m.team t", Member.class).getResultList();  // 내부 조인 inner 생략 가능


            //on join 조건문이다
           //List<Member> result = em.createQuery("select m from Member m left join m.team t on t.name = '마드리드'", Member.class).getResultList();

            //연관관계 없는 join ....회원의 이름과 팀이름이 같은 대상 외부 조인  // 막조인은 TEAM을 따로 선언해야 한다
            //List<Member> result = em.createQuery("select m from Member m left join Team t on m.username = t.name",
                    //Member.class).getResultList();




            /*
            // enum은 패키지 까지 적어야 한단 1번 방법
            String query ="select m.username , 'Hello' , true From Member m "+ "where m.type = jpql.MemberType.ADMIN";  // ADMIN 타입의 유저만 조회
            List<Object[]> result = em.createQuery(query).getResultList();

            //2번 방법
            String query ="select m.username , 'Hello' , true From Member m "+ "where m.type = userType";  // ADMIN 타입의 유저만 조회
            List<Object[]> result = em.createQuery(query).setParameter("userType",MemberType.ADMIN).getResultList();

            for (Object[] objects : result) {
                System.out.println("objects[0] = " + objects[0]);
                System.out.println("objects[0] = " + objects[1]);
                System.out.println("objects[0] = " + objects[2]);
            }

             */




            /*
            // 뛰어쓰기 조심  case 식
            String query = "select "+
                    "case when m.age <= 10 then '학생요금' "+
                        "when m.age >= 60 then '경로요금' "+
                        "else '일반요금 ' "+
                        "end " +
                        "from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);

            }

             */




            /*
            //coalesce 하나씩 조회해서 null이 아니면 반환
            String query = "select coalesce(m.username,'이름없는 회원') from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);

            }
            */




            /*

            //null if 두 값이 같으면 null 반환 , 다르면 첫번째 값 반환
            String query = "select nullif(m.username,'관리자') from Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
                
            }

            */




            /*
            //JPQL 기본 함수

//            String query = "select  concat('a','b') from Member m";  // 더해주는 함수
//
//            String query = "select  substring(m.username,2,3) from Member m";  //자르기
//
//            trim은 공백 없애기
//
//            String query = "select locate('de','abcdefg') from Member m";  // List<String> 를 Integer로 바꾸어야한다 숫자를 반환해주기 때문에
//
//            String query = "select size(t.members) from Team t";  // Team 컬렉션에 값을 넣어 줘야 나온다 팀이름 이런거
//
//            String query = "select index(t.members) from Team t";

            String query = "select function('group_concat', m.username) from Member m";

            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);

            }

             */




            // =========경로 표현식 특징========== //

            /*
            // manytoone
            String query = "select m From Member m join fetch m.team ";  // 이떄는 프록시가 아닌 진짜 데이터 엔티티이다
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println(" member= " + member.getUsername()+", " + member.getTeam().getName());
                // 회원 1 팀A(sql)
                // 회원 2 팀A(이미 영속성 1차 캐쉬)
                // 회원 3 팀B(SQL)

                //회원 100명-->N + 1  이런 오류가 발생한다 그러면 LAZY 이런거 해도 발생한다  그래서 JOIN FETCH 사용
            }
             */



            /*
            //일대다 관계, 컬렉션 페치 조회에서 중복으로 나오게 된다
            String query = "select distinct t From Team t join fetch t.members";
            List<Team> result = em.createQuery(query,Team.class).getResultList();

            System.out.println("result.size() = " + result.size());


            for (Team team : result) {
                System.out.println("team= " + team.getName() + "|" + team.getMembers().size());

                for(Member member : team.getMembers()){
                    System.out.println("member = " + member);
                }
            }

             */




            /*
            // entity 직접 사용
            String query = "select m From Member m where m = :member ";  // 이떄는 프록시가 아닌 진짜 데이터 엔티티이다
            Member findMember = em.createQuery(query, Member.class).setParameter("member", member1).getSingleResult();
            System.out.println("findMember = " + findMember);

             */


            tx.commit();

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();

    }
}
