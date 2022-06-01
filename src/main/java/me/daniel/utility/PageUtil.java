package me.daniel.utility;

/**
 * 페이징 처리 util class
 */
public class PageUtil {

    private PageUtil(){

    }

    /**
     * 시작 행 구하는 메서드
     * @param pageNum 현재 페이지 번호
     * @param size 한번에 표시할 리스트의 목록 개수
     * @return 시작 행 번호
     */
    public static int getStartRow(int pageNum, int size) {
        return (pageNum - 1) * size + 1;
    }
}
