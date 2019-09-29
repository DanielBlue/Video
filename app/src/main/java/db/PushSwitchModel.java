package db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by JC on 2019/9/18.
 */
@Table(database =AppDataBase.class)
public class PushSwitchModel extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private int likechoose;
    @Column
    private int attentionchoose;
    @Column
    private int commentchoose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikechoose() {
        return likechoose;
    }

    public void setLikechoose(int likechoose) {
        this.likechoose = likechoose;
    }

    public int getAttentionchoose() {
        return attentionchoose;
    }

    public void setAttentionchoose(int attentionchoose) {
        this.attentionchoose = attentionchoose;
    }

    public int getCommentchoose() {
        return commentchoose;
    }

    public void setCommentchoose(int commentchoose) {
        this.commentchoose = commentchoose;
    }
}
