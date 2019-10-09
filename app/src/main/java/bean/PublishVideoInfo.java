package bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class PublishVideoInfo implements Serializable{
    String coverFile;
    String videoDuraion;
    String videowidth;
    String videoheight;
    String location;

    public String getLocalurl() {
        return localurl;
    }

    public void setLocalurl(String localurl) {
        this.localurl = localurl;
    }

    Bitmap bitmap;
    String localurl;
    public String getCoverFile() {
        return coverFile;
    }

    public void setCoverFile(String coverFile) {
        this.coverFile = coverFile;
    }

    public String getVideoDuraion() {
        return videoDuraion;
    }

    public void setVideoDuraion(String videoDuraion) {
        this.videoDuraion = videoDuraion;
    }

    public String getVideowidth() {
        return videowidth;
    }

    public void setVideowidth(String videowidth) {
        this.videowidth = videowidth;
    }

    public String getVideoheight() {
        return videoheight;
    }

    public void setVideoheight(String videoheight) {
        this.videoheight = videoheight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "PublishVideoInfo{" +
                "coverFile='" + coverFile + '\'' +
                ", videoDuraion='" + videoDuraion + '\'' +
                ", videowidth='" + videowidth + '\'' +
                ", videoheight='" + videoheight + '\'' +
                ", location='" + location + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
