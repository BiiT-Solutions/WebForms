
    alter table tree_answers 
        add column image_ID bigint;

    alter table tree_blocks 
        add column image_ID bigint;

    alter table tree_categories 
        add column image_ID bigint;

    alter table tree_forms 
        add column image_ID bigint;

    alter table tree_questions 
        add column image_ID bigint;

    alter table tree_texts 
        add column image_ID bigint;

    alter table tree_answers 
        add constraint FK_ca7tnhk5rlyfghkc84jijy4ab 
        foreign key (image_ID) 
        references images (ID);

    alter table tree_blocks 
        add constraint FK_qcf2bnixooumn5o4hbd939m75 
        foreign key (image_ID) 
        references images (ID);

    alter table tree_categories 
        add constraint FK_elhhu7g8wxqr5b4fnrmg4hkba 
        foreign key (image_ID) 
        references images (ID);

    alter table tree_forms 
        add constraint FK_daxt5r43gbubcxv2tcacfp9kp 
        foreign key (image_ID) 
        references images (ID);

    alter table tree_questions 
        add constraint FK_igrsj2fuoe3jgojsldb9n03sd 
        foreign key (image_ID) 
        references images (ID);

    alter table tree_texts 
        add constraint FK_39600gq7say32ek6o957jhgo1 
        foreign key (image_ID) 
        references images (ID);
