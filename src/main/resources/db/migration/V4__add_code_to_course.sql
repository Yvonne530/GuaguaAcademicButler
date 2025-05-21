-- 修复 course 表，添加缺失的 code 列
ALTER TABLE course
    ADD COLUMN code VARCHAR(255);
