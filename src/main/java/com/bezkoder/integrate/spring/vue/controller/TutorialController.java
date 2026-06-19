package com.bezkoder.integrate.spring.vue.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.integrate.spring.vue.model.Tutorial;
import com.bezkoder.integrate.spring.vue.repository.TutorialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

    // 🎯 核心修正：就是少了這一行，補上它紅字就會全部消失！
    private static final Logger log = LoggerFactory.getLogger(TutorialController.class);

    @Autowired
    TutorialRepository tutorialRepository;

    // 🚀 最快實作：固得租車網頁「付款/送出」專用 API
    @PostMapping("/rentals")
    public ResponseEntity<Tutorial> createRentalFromForm(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String location,
            @RequestParam double amount,
            @RequestParam String payType,
            @RequestParam String cardNumber) {

        try {
            log.info("[固得租車-極速 API] 收到前端表單 ➔ 姓名: {}, 據點: {}, 金額: ${}", name, location, amount);

            // 1. 欄位打包組裝技術
            String packedTitle = name + "|" + phone;
            String packedDesc = location + "|" + amount + "|" + payType + "|" + cardNumber;

            // 2. 直接存入現有的 Tutorial 實體，完全不改既有資料庫架構
            Tutorial rentalRecord = new Tutorial(packedTitle, packedDesc, false);
            Tutorial savedRecord = tutorialRepository.save(rentalRecord);

            log.info("[固得租車-極速 API] 資料成功寫入 Tutorial 表！ID: {}", savedRecord.getId());
            return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("[固得租車-極速 API] 儲存失敗，錯誤原因: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🚀 查詢 API：依據「取車據點」關鍵字快速撈出 Tutorial 內包裝的訂單
    @GetMapping("/rentals/search")
    public ResponseEntity<List<Tutorial>> searchRentalsByLocation(@RequestParam String location) {
        try {
            log.info("[固得租車-極速 API] 後台搜尋據點關鍵字: {}", location);

            // 利用現有的 findByTitleContaining 或 findByDescriptionContaining
            List<Tutorial> result = tutorialRepository.findByDescriptionContaining(location);

            if (result.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/books")
    public ResponseEntity<List<Tutorial>> getAllBooks() {
        try {
            List<Tutorial> books = new ArrayList<Tutorial>();

            tutorialRepository.findAll().forEach(books::add);

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = new ArrayList<>();
            if (title == null) {
                tutorialRepository.findAll().forEach(tutorials::add);
            } else {
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
            }

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = tutorialRepository
                    .save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial

    ) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            Tutorial existingTutorial = tutorialData.get();
            existingTutorial.setTitle(tutorial.getTitle());
            existingTutorial.setDescription(tutorial.getDescription());
            existingTutorial.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(existingTutorial), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id

    ) {
        try {
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials

    () {
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished

    () {
        try {
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
