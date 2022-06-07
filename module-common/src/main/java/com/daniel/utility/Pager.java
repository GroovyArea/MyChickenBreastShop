package com.daniel.utility;

/**
 * 페이징 처리 util class
 */
public class Pager {

    private final String searchKeyword;
    private final String searchValue;
    private final int startRow;
    private final int rowCount;

    public Pager(String searchKeyword, String searchValue, int startRow, int rowCount) {
        this.searchKeyword = searchKeyword;
        this.searchValue = searchValue;
        this.startRow = startRow;
        this.rowCount = rowCount;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getRowCount() {
        return rowCount;
    }

    /**
     * 시작 행 구하는 메서드
     *
     * @param pageNum 현재 페이지 번호
     * @param size    한번에 표시할 리스트의 목록 개수
     * @return 시작 행 번호
     */
    public static int getStartRow(int pageNum, int size) {
        return (pageNum - 1) * size + 1;
    }
}
