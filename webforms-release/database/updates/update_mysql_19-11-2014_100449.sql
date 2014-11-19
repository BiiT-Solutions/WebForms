
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

    alter table token_between 
        drop constraint UK_qt1gj983c6e5r5ptwyiqxahb5;

    alter table token_between 
        add constraint UK_qt1gj983c6e5r5ptwyiqxahb5  unique (ID);

    alter table token_between 
        drop constraint UK_r9pxcwv0o4d8b4r75htyvdnkx;

    alter table token_between 
        add constraint UK_r9pxcwv0o4d8b4r75htyvdnkx  unique (comparationId);

    alter table token_in 
        drop constraint UK_ap3dned8u6o0obyr6cc79ju3;

    alter table token_in 
        add constraint UK_ap3dned8u6o0obyr6cc79ju3  unique (ID);

    alter table token_in 
        drop constraint UK_os6rq6y1tm4v0hodtajw6gk5b;

    alter table token_in 
        add constraint UK_os6rq6y1tm4v0hodtajw6gk5b  unique (comparationId);

    alter table token_in_value 
        drop constraint UK_i22eg0e2jvfqeelp7l9ycjylo;

    alter table token_in_value 
        add constraint UK_i22eg0e2jvfqeelp7l9ycjylo  unique (ID);

    alter table token_in_value 
        drop constraint UK_5f75q3eyx1mg10qmjxxpxdtwp;

    alter table token_in_value 
        add constraint UK_5f75q3eyx1mg10qmjxxpxdtwp  unique (comparationId);

    alter table token_between 
        add constraint FK_6fgr694hshlk5cxvpequy9im7 
        foreign key (question_ID) 
        references tree_questions (ID);

    alter table token_between 
        add constraint FK_ra954eotsl01s1s20r3iipvqv 
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
