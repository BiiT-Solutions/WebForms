package com.biit.webforms.persistence.entity;

public interface ElementWithMedia {

    void setImage(TreeObjectImage image);

    TreeObjectImage getImage();

    void setVideo(TreeObjectVideo video);

    TreeObjectVideo getVideo();

    void setAudio(TreeObjectAudio audio);

    TreeObjectAudio getAudio();
}
