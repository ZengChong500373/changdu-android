package org.linyi.viva.entity;

public class BookListEntity {
    private  String key;
  private   int maxPage;

    public BookListEntity(int currentPage) {
        this.currentPage = currentPage;
    }

    private int currentPage;

    public BookListEntity(String key, int currentPage) {
        this.key = key;
        this.currentPage = currentPage;
    }


    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
