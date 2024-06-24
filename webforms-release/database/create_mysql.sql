
    create table flow (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        flow_type varchar(255),
        others bit not null,
        destiny_id bigint,
        form_id bigint not null,
        origin_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    create table images (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        data longblob,
        file_name varchar(255),
        height integer not null,
        url TEXT,
        width integer not null,
        element_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table linked_form_versions (
       form_id bigint not null,
        linked_form_versions integer
    ) engine=InnoDB;

    create table token (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table token_between (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        question bigint,
        date_period_unit varchar(255),
        subformat varchar(255) not null,
        value_end varchar(255),
        value_start varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table token_comparation_answer (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        question bigint,
        answer bigint,
        primary key (id)
    ) engine=InnoDB;

    create table token_comparation_value (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        question bigint,
        date_period_unit varchar(255),
        subformat varchar(255) not null,
        value varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table token_empty (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        question bigint,
        date_period_unit varchar(255),
        subformat varchar(255) not null,
        value varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table token_in (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_sequence bigint not null,
        type varchar(255) not null,
        flow bigint not null,
        question bigint,
        primary key (id)
    ) engine=InnoDB;

    create table token_in_value (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        sort_seq bigint not null,
        answer_value bigint,
        token_in bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table tree_answers (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        description varchar(10000),
        primary key (id)
    ) engine=InnoDB;

    create table tree_attached_files (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        description TEXT,
        edition_disabled bit,
        mandatory bit not null,
        primary key (id)
    ) engine=InnoDB;

    create table tree_blocks (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        description longtext,
        edition_disabled bit default 0 not null,
        json longtext,
        linked_form_label varchar(255),
        linked_form_organization_id bigint,
        status varchar(255),
        form_reference bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_blocks_references (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        reference bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table tree_categories (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_dynamic_answer (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        reference bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_forms (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        description longtext,
        edition_disabled bit default 0 not null,
        json longtext,
        linked_form_label varchar(255),
        linked_form_organization_id bigint,
        status varchar(255),
        form_reference bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_forms_references_hidden_elements (
       form bigint not null,
        element_to_hide bigint not null,
        primary key (form, element_to_hide)
    ) engine=InnoDB;

    create table tree_groups (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        repeatable bit not null,
        number_of_column int default 1 not null,
        is_table bit not null,
        total_answers_value bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_questions (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        abbreviation varchar(100),
        alias varchar(100),
        answer_format varchar(255),
        answer_subformat varchar(255),
        answer_type varchar(255),
        default_value_string TEXT,
        default_value_time datetime(6),
        description TEXT,
        edition_disabled bit default 1 not null,
        horizontal bit not null,
        mandatory bit not null,
        default_value_answer bigint,
        max_answers bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_system_fields (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    ) engine=InnoDB;

    create table tree_texts (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        description varchar(10000),
        primary key (id)
    ) engine=InnoDB;

    create table user_tokens (
       userId bigint not null,
        knowledge_manager_auth_token varchar(255),
        primary key (userId)
    ) engine=InnoDB;

    create table webservice_call (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        name varchar(255) not null,
        webservice_name varchar(255),
        form bigint not null,
        form_element_trigger bigint,
        primary key (id)
    ) engine=InnoDB;

    create table webservice_call_input_link (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        webservice_port varchar(250),
        form_element bigint,
        validation_xpath varchar(255),
        webservice_call bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table webservice_call_input_link_errors (
       id bigint not null,
        error_code varchar(255),
        error_message varchar(255),
        webservice_call_input_link bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table webservice_call_output_link (
       id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime(6) not null,
        update_time datetime(6),
        updated_by DOUBLE,
        webservice_port varchar(250),
        form_element bigint,
        editable bit,
        webservice_call bigint not null,
        primary key (id)
    ) engine=InnoDB;

    alter table flow 
       add constraint UK_f2lbg87w4l8ckurvqn29yhw5u unique (comparation_id);

    alter table images 
       add constraint UK_q68t2ifi5ad0ohm85eqk9jbu5 unique (element_id);

    alter table images 
       add constraint UK_dy9n7qskcu76kw98po10wqkgs unique (comparation_id);

    alter table linked_form_versions 
       add constraint UKk6vfb9l7mjmvtsnprdh4ncyh7 unique (form_id, linked_form_versions);

    alter table token 
       add constraint UK_n5llyb0plj8fyj0xra95qsjw7 unique (comparation_id);

    alter table token_between 
       add constraint UK_7393phfyfur3ypdj6lh7wcwyr unique (id);

    alter table token_between 
       add constraint UK_f80vpw5r8wr8ob7fwvfv9db8f unique (comparation_id);

    alter table token_comparation_answer 
       add constraint UK_1q0mna2llubobh3uuu1kmtha4 unique (id);

    alter table token_comparation_answer 
       add constraint UK_3hrlvy8i3jgq156hju7193qbo unique (comparation_id);

    alter table token_comparation_value 
       add constraint UK_1nxjs9edjvkn1i9d14315dkni unique (id);

    alter table token_comparation_value 
       add constraint UK_f16xon5c6ikammnr4wtau299n unique (comparation_id);

    alter table token_empty 
       add constraint UK_ju10mhk2l40w0c9vah843iy6q unique (id);

    alter table token_empty 
       add constraint UK_eif5dp9meva9dbfbu1ybjuvbi unique (comparation_id);

    alter table token_in 
       add constraint UK_etg9s4o7mwaiicwejxjab260a unique (id);

    alter table token_in 
       add constraint UK_45pd168dkiq2lm490jwcunwal unique (comparation_id);

    alter table token_in_value 
       add constraint UK_7plyot15t7efka3kxkgkh4dlx unique (comparation_id);

    alter table tree_answers 
       add constraint UK_dktggosx7ohcksfob4k94bfmb unique (id);

    alter table tree_answers 
       add constraint UK_novq229qj7ibt96gyqw251biu unique (comparation_id);

    alter table tree_attached_files 
       add constraint UK_bdku5oqe5h31rqfvrtrqih6mh unique (id);

    alter table tree_attached_files 
       add constraint UK_ntgqwc8vjm9nlv4f5s1bb2v3h unique (comparation_id);

    alter table tree_blocks 
       add constraint UK_kfl0ygxd3kmomv1x5s3uy1ro4 unique (id);

    alter table tree_blocks 
       add constraint UK_lmu3cq3olf1t5gmi39cf1aac unique (comparation_id);

    alter table tree_blocks_references 
       add constraint UK_h6705gqc3ly6jvk0gr2net9wk unique (id);

    alter table tree_blocks_references 
       add constraint UK_nuir1gkk1iqhv8mtvj4k9vrw7 unique (comparation_id);

    alter table tree_categories 
       add constraint UK_t6nmk0eqg7yvg78lxqlrtdr66 unique (id);

    alter table tree_categories 
       add constraint UK_1xpeifv5qxo29x7yhpfmjtbyo unique (comparation_id);

    alter table tree_dynamic_answer 
       add constraint UK_5y5wyxigj8ukynqia8jfbwe9c unique (id);

    alter table tree_dynamic_answer 
       add constraint UK_oeg54oeq04pihalip3b3479vp unique (comparation_id);

    alter table tree_forms 
       add constraint UK89ul4cb5nvq3aaiyni9w6dqaq unique (label, version, organization_id);

    alter table tree_forms 
       add constraint UK_765yjtcx0oa8unb588ngimaml unique (id);

    alter table tree_forms 
       add constraint UK_t05hap53xy8005w0etx1tm0yx unique (comparation_id);

    alter table tree_groups 
       add constraint UK_lkx000598o8fu6o45gc6j8j6h unique (id);

    alter table tree_groups 
       add constraint UK_544k9wyac47tkk3mh7itf8h9f unique (comparation_id);

    alter table tree_questions 
       add constraint UK_qmnu5ia8n490ok4d7obj7khk6 unique (id);

    alter table tree_questions 
       add constraint UK_589h63s3jthrsckwd8a4dn3xq unique (comparation_id);

    alter table tree_system_fields 
       add constraint UK_bnumacfcvsl95y2gnfn4so2op unique (id);

    alter table tree_system_fields 
       add constraint UK_f0b017j2vmq9p9kga9bq7o85x unique (comparation_id);

    alter table tree_texts 
       add constraint UK_evv83frsh83pykib24ywsb5pm unique (id);

    alter table tree_texts 
       add constraint UK_jgk76ykdcnwih8xjeno3ibtbp unique (comparation_id);

    alter table webservice_call 
       add constraint UK_s9sdg0nbynxb17f5xenv24wov unique (comparation_id);

    alter table webservice_call_input_link 
       add constraint UK_eyb62ax0biq5p0xlp9u3wqoo8 unique (id);

    alter table webservice_call_input_link 
       add constraint UK_t78d3ptjxutqtos6q8k1ei3nb unique (comparation_id);

    alter table webservice_call_output_link 
       add constraint UK_t5weay02d8vl513y6nlvqp37 unique (id);

    alter table webservice_call_output_link 
       add constraint UK_a32de2wku4rhijgxgsr96ps0i unique (comparation_id);

    alter table token 
       add constraint FK76wfpgkgyfhteuadtakv5c353 
       foreign key (flow) 
       references flow (id);

    alter table token_between 
       add constraint FK_ltqb9si4d4fff9inaj1710g4 
       foreign key (flow) 
       references flow (id);

    alter table token_comparation_answer 
       add constraint FKb00o30i4wdtm5mxquae66xxib 
       foreign key (answer) 
       references tree_answers (id);

    alter table token_comparation_answer 
       add constraint FK_ihdep1i36ddlnhfs7a4fbk4d5 
       foreign key (flow) 
       references flow (id);

    alter table token_comparation_value 
       add constraint FK_5ela5pdlkhjlbfeoip70ry7bf 
       foreign key (flow) 
       references flow (id);

    alter table token_empty 
       add constraint FK_pwqr169ufwb9buif8ts1nidvn 
       foreign key (flow) 
       references flow (id);

    alter table token_in 
       add constraint FK_n0wcrcuwekihl1l9pujhqhisa 
       foreign key (flow) 
       references flow (id);

    alter table token_in_value 
       add constraint FK9fde33yhkjjjer5yq3sy4ek7c 
       foreign key (answer_value) 
       references tree_answers (id);

    alter table token_in_value 
       add constraint FKkwtyuwwpti64ksq780ckic623 
       foreign key (token_in) 
       references token_in (id);

    alter table tree_blocks_references 
       add constraint FKens64mptob1hcjps0ardt9brv 
       foreign key (reference) 
       references tree_blocks (id);

    alter table tree_dynamic_answer 
       add constraint FKsi8pnf26jpi3tnxweqy3wlv47 
       foreign key (reference) 
       references tree_questions (id);

    alter table tree_questions 
       add constraint FK6ecocpmhot51wopw79e9x7swc 
       foreign key (default_value_answer) 
       references tree_answers (id);

    alter table webservice_call_input_link 
       add constraint FK141cechf1ax8cl1oywpqywaml 
       foreign key (webservice_call) 
       references webservice_call (id);

    alter table webservice_call_input_link_errors 
       add constraint FKqipl8qhjbg4ia03buv0bf7743 
       foreign key (webservice_call_input_link) 
       references webservice_call_input_link (id);

    alter table webservice_call_output_link 
       add constraint FKepunk08nh70l77a705cgyxqpl 
       foreign key (webservice_call) 
       references webservice_call (id);

