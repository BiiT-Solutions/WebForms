
    create table flow (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        flowType varchar(255),
        others bit not null,
        destiny_id bigint,
        form_ID bigint,
        origin_id bigint not null,
        primary key (ID)
    );

    create table linked_form_versions (
        formId bigint not null,
        linkedFormVersions integer
    );

    create table token (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255) not null,
        flow_ID bigint,
        primary key (ID)
    );

    create table token_between (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255) not null,
        flow_ID bigint,
        datePeriodUnit varchar(255),
        subformat varchar(255) not null,
        valueEnd varchar(255),
        valueStart varchar(255),
        question_ID bigint not null,
        primary key (ID)
    );

    create table token_comparation_answer (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255) not null,
        flow_ID bigint,
        answer_ID bigint not null,
        question_ID bigint not null,
        primary key (ID)
    );

    create table token_comparation_value (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255) not null,
        flow_ID bigint,
        datePeriodUnit varchar(255),
        subformat varchar(255) not null,
        value varchar(255),
        question_ID bigint,
        primary key (ID)
    );

    create table token_in (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        type varchar(255) not null,
        flow_ID bigint,
        question_ID bigint not null,
        primary key (ID)
    );

    create table token_in_value (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        sortSeq bigint not null,
        answerValue_ID bigint,
        tokenIn_ID bigint,
        primary key (ID)
    );

    create table tree_answers (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        description varchar(10000),
        primary key (ID)
    );

    create table tree_blocks (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        organizationId DOUBLE not null,
        version integer,
        description longtext,
        linkedFormLabel varchar(255),
        linkedFormOrganizationId bigint,
        status varchar(255),
        primary key (ID)
    );

    create table tree_blocks_references (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        reference_ID bigint,
        primary key (ID)
    );

    create table tree_categories (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table tree_forms (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        organizationId DOUBLE not null,
        version integer,
        description longtext,
        linkedFormLabel varchar(255),
        linkedFormOrganizationId bigint,
        status varchar(255),
        primary key (ID)
    );

    create table tree_groups (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        repeatable bit not null,
        primary key (ID)
    );

    create table tree_questions (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        answerFormat varchar(255),
        answerSubformat varchar(255),
        answerType varchar(255),
        description varchar(10000),
        horizontal bit not null,
        mandatory bit not null,
        primary key (ID)
    );

    create table tree_system_fields (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        primary key (ID)
    );

    create table tree_texts (
        ID bigint not null,
        comparationId varchar(190) not null,
        createdBy DOUBLE,
        creationTime datetime not null,
        updateTime datetime,
        updatedBy DOUBLE,
        label varchar(1000),
        name varchar(190),
        originalReference varchar(190) not null,
        sortSeq bigint not null,
        parent_ID bigint,
        description varchar(10000),
        primary key (ID)
    );

    alter table flow 
        add constraint UK_kffbghchm5cku772ng2gmaofa  unique (ID);

    alter table flow 
        add constraint UK_ncm3pmigu960w0cbnc8cpi9ip  unique (comparationId);

    alter table linked_form_versions 
        add constraint UK_c87p50fduqtda2111ese0s580  unique (formId, linkedFormVersions);

    alter table token 
        add constraint UK_cw25p7uvbd3f5kb07q97haaho  unique (ID);

    alter table token 
        add constraint UK_7adviq7imnx5f4hfitaywu9ba  unique (comparationId);

    alter table token_between 
        add constraint UK_qt1gj983c6e5r5ptwyiqxahb5  unique (ID);

    alter table token_between 
        add constraint UK_r9pxcwv0o4d8b4r75htyvdnkx  unique (comparationId);

    alter table token_comparation_answer 
        add constraint UK_n8snmhcqi2j07dp4sonrmnwv7  unique (ID);

    alter table token_comparation_answer 
        add constraint UK_k8hfe94svgroao3ro57m60ptl  unique (comparationId);

    alter table token_comparation_value 
        add constraint UK_bh6lvajnm5h8vhj2ef632fbct  unique (ID);

    alter table token_comparation_value 
        add constraint UK_6jhrnt7aar8o5gycyy2hr91dk  unique (comparationId);

    alter table token_in 
        add constraint UK_ap3dned8u6o0obyr6cc79ju3  unique (ID);

    alter table token_in 
        add constraint UK_os6rq6y1tm4v0hodtajw6gk5b  unique (comparationId);

    alter table token_in_value 
        add constraint UK_i22eg0e2jvfqeelp7l9ycjylo  unique (ID);

    alter table token_in_value 
        add constraint UK_5f75q3eyx1mg10qmjxxpxdtwp  unique (comparationId);

    alter table tree_answers 
        add constraint UK_413vxa542h86uqy4uvcnv6y2x  unique (ID);

    alter table tree_answers 
        add constraint UK_5xuj3de6ide6evpo4sijpqa4o  unique (comparationId);

    alter table tree_blocks 
        add constraint UK_f965i4ecegx997gfv8w2tij8t  unique (ID);

    alter table tree_blocks 
        add constraint UK_5yv2t9uoy5skfid1s6p1i6tay  unique (comparationId);

    alter table tree_blocks 
        add constraint UK_r7xwfhjrx5jwbwhtd8kyp563s  unique (label, version, organizationId);

    alter table tree_blocks_references 
        add constraint UK_c8un1l38a1eplwrsrc1j45i9a  unique (ID);

    alter table tree_blocks_references 
        add constraint UK_7wyh0kt2iv3j3w9mxvi12v3tq  unique (comparationId);

    alter table tree_categories 
        add constraint UK_ec3bvy7lletc6jmyvyfwuroqv  unique (ID);

    alter table tree_categories 
        add constraint UK_gtcyh8mle277igwtb5dvhjkr1  unique (comparationId);

    alter table tree_forms 
        add constraint UK_gyfbqpo5jwnsoftogc0bs77k0  unique (label, version, organizationId);

    alter table tree_forms 
        add constraint UK_plkq2e2pj19uak2ncrgf1ft6v  unique (ID);

    alter table tree_forms 
        add constraint UK_k9mhkly9g8lqwf1m9esm50y6m  unique (comparationId);

    alter table tree_groups 
        add constraint UK_sfdvxxi1k3p9pqsjl5nhmgdp  unique (ID);

    alter table tree_groups 
        add constraint UK_sno2xl7o9nxmt3xh48ywus36u  unique (comparationId);

    alter table tree_questions 
        add constraint UK_9lkt55st6up2vyh38lrmu0dc5  unique (ID);

    alter table tree_questions 
        add constraint UK_nu1epukynjltak450rhyp6eu0  unique (comparationId);

    alter table tree_system_fields 
        add constraint UK_4qh5vx93d0480hg3j76p7oexr  unique (ID);

    alter table tree_system_fields 
        add constraint UK_r0il4g95ge6177s84s50icaui  unique (comparationId);

    alter table tree_texts 
        add constraint UK_gh4f3qqt3q8ak4wvj0kptc7x2  unique (ID);

    alter table tree_texts 
        add constraint UK_qe6275omm38jd6s7q9frwpdet  unique (comparationId);

    alter table token 
        add constraint FK_1jkae8phlxx6soqblc3rk1s04 
        foreign key (flow_ID) 
        references flow (ID);

    alter table token_between 
        add constraint FK_6fgr694hshlk5cxvpequy9im7 
        foreign key (question_ID) 
        references tree_questions (ID);

    alter table token_between 
        add constraint FK_ra954eotsl01s1s20r3iipvqv 
        foreign key (flow_ID) 
        references flow (ID);

    alter table token_comparation_answer 
        add constraint FK_6y1oe55eikigt0ncjjeh6kueo 
        foreign key (answer_ID) 
        references tree_answers (ID);

    alter table token_comparation_answer 
        add constraint FK_9l6aua9xkdoc2m5uve59k84kp 
        foreign key (question_ID) 
        references tree_questions (ID);

    alter table token_comparation_answer 
        add constraint FK_qjip50m8a6wkqu9wdqw828a9l 
        foreign key (flow_ID) 
        references flow (ID);

    alter table token_comparation_value 
        add constraint FK_8p15e0dwtviut3r9rikqme7xq 
        foreign key (question_ID) 
        references tree_questions (ID);

    alter table token_comparation_value 
        add constraint FK_m1g8tiej2sbbgy2y4xoyw6g7x 
        foreign key (flow_ID) 
        references flow (ID);

    alter table token_in 
        add constraint FK_d2lp2gbfwmmp4l5td2730vkaf 
        foreign key (question_ID) 
        references tree_questions (ID);

    alter table token_in 
        add constraint FK_p9a5xftusa2qj6w2uxujwmshf 
        foreign key (flow_ID) 
        references flow (ID);

    alter table token_in_value 
        add constraint FK_aysu47tqxey50xwwod5kki3nd 
        foreign key (answerValue_ID) 
        references tree_answers (ID);

    alter table token_in_value 
        add constraint FK_1ja0iuwo6243fkd998qr2uwds 
        foreign key (tokenIn_ID) 
        references token_in (ID);

    alter table tree_blocks_references 
        add constraint FK_c2brjl6vkdwug1svsxaf3vol3 
        foreign key (reference_ID) 
        references tree_blocks (ID);

	CREATE TABLE `hibernate_sequence` (
		`next_val` bigint(20) DEFAULT NULL
	);

	LOCK TABLES `hibernate_sequence` WRITE;
	INSERT INTO `hibernate_sequence` VALUES (1);
	UNLOCK TABLES;

