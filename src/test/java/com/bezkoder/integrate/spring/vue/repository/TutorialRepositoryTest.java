
import com.bezkoder.integrate.spring.vue.model.Tutorial;
import com.bezkoder.integrate.spring.vue.repository.TutorialRepository;

import org.junit.jupiter.api.Test; // 💡 修正：解開註解，測試才能執行
import org.springframework.beans.factory.annotation.Autowired; // 💡 修正：保留一份就好，刪除重複
import org.springframework.boot.test.context.SpringBootTest; // 💡 修正：解開註解，才能啟動 Spring 環境
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat; // 💡 修正：解開註解並移到上方標準區塊

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class TutorialRepositoryTest {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Test
    public void testSaveAndFindTutorials() {
        // 1. 建立測試資料 List
        List<Tutorial> testList = new ArrayList<>();

        Tutorial t1 = new Tutorial();
        t1.setTitle("Spring Boot Guide");
        t1.setDescription("Description 1");
        t1.setPublished(true);

        Tutorial t2 = new Tutorial();
        t2.setTitle("Vue.js Integration");
        t2.setDescription("Description 2");
        t2.setPublished(false);

        testList.add(t1);
        testList.add(t2);

        // 2. 執行寫入
        for (Tutorial tutorial : testList) {
            // 💡 提示：如果此處報錯，請確認你 TutorialRepository 裡的寫入方法名稱是否為 insert
            tutorialRepository.save(tutorial);
        }

        // 3. 查詢驗證
        List<Tutorial> foundTutorials = tutorialRepository.findByTitleContaining("Spring");

        // 4. 斷言驗證結果
        assertThat(foundTutorials).isNotEmpty();
        assertThat(foundTutorials.get(0).getTitle()).contains("Spring");

        // 5. Console Log 印出結果
        System.out.println("==============================================");
        System.out.println("====== 成功從資料庫撈出測試資料 ======");
        foundTutorials.forEach(t -> System.out.println("撈出的標題: " + t.getTitle() + " | 描述: " + t.getDescription()));
        System.out.println("==============================================");
    }
}