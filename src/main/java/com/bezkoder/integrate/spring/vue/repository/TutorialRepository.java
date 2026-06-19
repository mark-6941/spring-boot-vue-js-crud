package com.bezkoder.integrate.spring.vue.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bezkoder.integrate.spring.vue.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  // 1. 既有的發布狀態查詢
  List<Tutorial> findByPublished(boolean published);

  // 2. 既有的標題模糊查詢（對應固得租車的「姓名|電話」）
  List<Tutorial> findByTitleContaining(String title);

  // 🎯 核心修正 1：補上這個方法，解決 Controller 報錯不能編譯的問題！
  List<Tutorial> findByDescriptionContaining(String description);

  // 🎯 核心修正 2：請「刪除」原本的 void insert(Tutorial tutorial);
  // 因為 JpaRepository 內建的寫入方法叫做 .save(entity)，不需要自己宣告 insert。
}