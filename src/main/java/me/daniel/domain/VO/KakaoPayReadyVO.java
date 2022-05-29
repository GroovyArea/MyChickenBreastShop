package me.daniel.domain.VO;

import java.beans.ConstructorProperties;
import java.util.Date;

public class KakaoPayReadyVO {

    private String tid, next_redirect_pc_url;
    private Date created_at;

    @ConstructorProperties({"tid","next_redirect_pc_url","created_at"})
    public KakaoPayReadyVO(String tid, String next_redirect_pc_url, Date created_at) {
        this.tid = tid;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.created_at = created_at;
    }

    public String getTid() {
        return tid;
    }

    public String getNext_redirect_pc_url() {
        return next_redirect_pc_url;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
