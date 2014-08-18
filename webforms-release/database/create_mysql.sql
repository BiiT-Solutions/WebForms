
    create table BaseAnswer (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table BaseCategory (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table BaseForm (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        version integer,
        primary key (ID)
    );

    create table BaseGroup (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        repetable bit not null,
        primary key (ID)
    );

    create table BaseQuestion (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table PARENT_OF_CHILDREN (
        TreeObject_ID bigint not null,
        children_ID bigint not null
    );

    create table TREE_ANSWERS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table TREE_CATEGORIES (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table TREE_FORMS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        version integer,
        availableFrom datetime not null,
        availableTo datetime,
        primary key (ID)
    );

    create table TREE_GROUPS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        repetable bit not null,
        primary key (ID)
    );

    create table TREE_QUESTIONS (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(255),
        name varchar(185),
        sortSeq bigint not null,
        parent_ID bigint,
        answerFormat varchar(255),
        answerType varchar(255),
        primary key (ID)
    );

    alter table BaseAnswer 
        add constraint UK_76lq0gmnjriqwvt0axvjt4h8s  unique (ID);

    alter table BaseAnswer 
        add constraint UK_djol5d7vg4wvgutb2l6cj5p3y  unique (comparationId);

    alter table BaseCategory 
        add constraint UK_ey647aoo30r3uqxirrm3v7jwa  unique (ID);

    alter table BaseCategory 
        add constraint UK_l6dm3qlnaolwpi2k56utdasqr  unique (comparationId);

    alter table BaseForm 
        add constraint UK_3n8x6f9irekyp7of2b7gibpio  unique (name, version);

    alter table BaseForm 
        add constraint UK_9mtlfju71ni1foihstunmc72e  unique (ID);

    alter table BaseForm 
        add constraint UK_g7segt6l5tegemkbp78vk02bc  unique (comparationId);

    alter table BaseGroup 
        add constraint UK_2jv9tjr1cl5ijdaueliw7ye25  unique (ID);

    alter table BaseGroup 
        add constraint UK_2ps7c6cq37l6c3672phkxkhc1  unique (comparationId);

    alter table BaseQuestion 
        add constraint UK_45n10petyj45qbfliy3nruklb  unique (ID);

    alter table BaseQuestion 
        add constraint UK_3m9ci7rb0u3owmafk0xh5w8bp  unique (comparationId);

    alter table PARENT_OF_CHILDREN 
        add constraint UK_oj9m32v898q47n973v4c4hmgi  unique (children_ID);

    alter table TREE_ANSWERS 
        add constraint UK_l02x1dnxdw4gm1tdlnrtxc6h4  unique (ID);

    alter table TREE_ANSWERS 
        add constraint UK_nsvxs27d466w94yohh36oasrb  unique (comparationId);

    alter table TREE_CATEGORIES 
        add constraint UK_qx48maxgp7r9qkh11jalq0794  unique (ID);

    alter table TREE_CATEGORIES 
        add constraint UK_lxhltjmdib7s0qhylpqagu3qm  unique (comparationId);

    alter table TREE_FORMS 
        add constraint UK_sqmslpiklr6yoh5mdndj1xqy4  unique (ID);

    alter table TREE_FORMS 
        add constraint UK_t7shqct5neaftl77sb3o2dw3b  unique (comparationId);

    alter table TREE_FORMS 
        add constraint UK_59cv8u9v3kckrnbt63cdxya75  unique (name, version);

    alter table TREE_GROUPS 
        add constraint UK_l8g7b24ir43unomkfe55fcimo  unique (ID);

    alter table TREE_GROUPS 
        add constraint UK_hwgdl270ks71y5qt3p3gvnqq9  unique (comparationId);

    alter table TREE_QUESTIONS 
        add constraint UK_fkqtw0jhv95ld1t98wsn6ul2u  unique (ID);

    alter table TREE_QUESTIONS 
        add constraint UK_7t4bn32ohjfi0my4tsxuswh3v  unique (comparationId);
