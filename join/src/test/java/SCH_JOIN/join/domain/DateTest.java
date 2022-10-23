package SCH_JOIN.join.domain;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest
public class DateTest {

    @Test
    public void dateTest(){

        LocalDateTime date1 = LocalDateTime.of(2020, 5, 4,17,30,0);
        LocalDateTime date2 = LocalDateTime.of(2020, 5, 4,19,30,0);


        System.out.println("date1 = " + date1);
        System.out.println("date1 = " + date2);

        List<LocalDateTime> list = new ArrayList<>();
        list.add(date1);
        list.add(date2);
        list.forEach(item -> System.out.println(item.toString()));




    }
}
