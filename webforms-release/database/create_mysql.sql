
    create table flow (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        flowType varchar(255),
        others bit not null,
        destiny_id bigint,
        form_id bigint not null,
        origin_id bigint,
        primary key (id)
    );

    create table images (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        data longblob,
        file_name varchar(255),
        height integer not null,
        url TEXT,
        width integer not null,
        element_id bigint not null,
        primary key (id)
    );

    create table linked_form_versions (
        form_id bigint not null,
        linked_form_versions integer
    );

    create table token (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        type varchar(255) not null,
        flow_id bigint not null,
        primary key (id)
    );

    create table token_between (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        type varchar(255) not null,
        flow_id bigint not null,
        question bigint,
        date_period_unit varchar(255),
        subformat varchar(255) not null,
        value_end varchar(255),
        value_start varchar(255),
        primary key (id)
    );

    create table token_comparation_answer (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        type varchar(255) not null,
        flow_id bigint not null,
        question bigint,
        answer_id bigint,
        primary key (id)
    );

    create table token_comparation_value (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        type varchar(255) not null,
        flow_id bigint not null,
        question bigint,
        date_period_unit varchar(255),
        subformat varchar(255) not null,
        value varchar(255),
        primary key (id)
    );

    create table token_in (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        type varchar(255) not null,
        flow_id bigint not null,
        question bigint,
        primary key (id)
    );

    create table token_in_value (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        sort_seq bigint not null,
        answer_value bigint,
        token_in bigint not null,
        primary key (id)
    );

    create table tree_answers (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        description varchar(10000),
        primary key (id)
    );

    create table tree_blocks (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        description longtext,
        edition_disabled bit,
        linked_form_label varchar(255),
        linked_form_organization_id bigint,
        status varchar(255),
        form_reference_id bigint,
        primary key (id)
    );

    create table tree_blocks_references (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        reference_id bigint not null,
        primary key (id)
    );

    create table tree_categories (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    );

    create table tree_dynamic_answer (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        reference_id bigint,
        primary key (id)
    );

    create table tree_forms (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(190),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        organization_id DOUBLE not null,
        version integer,
        description longtext,
        edition_disabled bit,
        linked_form_label varchar(255),
        linked_form_organization_id bigint,
        status varchar(255),
        form_reference_id bigint,
        primary key (id)
    );

    create table tree_forms_references_hidden_elements (
        tree_forms_id bigint not null,
        elementsToHide_id bigint not null,
        primary key (tree_forms_id, elementsToHide_id)
    );

    create table tree_groups (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        repeatable bit not null,
        primary key (id)
    );

    create table tree_questions (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        answer_format varchar(255),
        answer_subformat varchar(255),
        answer_type varchar(255),
        default_value_string TEXT,
        default_value_time datetime,
        description TEXT,
        edition_disabled bit,
        horizontal bit not null,
        mandatory bit not null,
        default_value_answer bigint,
        primary key (id)
    );

    create table tree_system_fields (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        primary key (id)
    );

    create table tree_texts (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        label varchar(1000),
        name varchar(190),
        original_reference varchar(190) not null,
        sort_sequence bigint not null,
        parent bigint,
        description varchar(10000),
        primary key (id)
    );

    create table webservice_call (
        id bigint not null auto_increment,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        name varchar(255) not null,
        webserviceName varchar(255),
        form_id bigint not null,
        formElementTrigger_id bigint,
        primary key (id)
    );

    create table webservice_call_input_link (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        webservicePort varchar(250),
        formElement_id bigint,
        validationXpath varchar(255),
        webserviceCall_id bigint not null,
        primary key (id)
    );

    create table webservice_call_input_link_errors (
        ID bigint not null auto_increment,
        errorCode varchar(255),
        errorMessage varchar(255),
        webserviceCallInputLink_id bigint not null,
        primary key (ID)
    );

    create table webservice_call_output_link (
        id bigint not null,
        comparation_id varchar(190) not null,
        created_by DOUBLE,
        creation_time datetime not null,
        update_time datetime,
        updated_by DOUBLE,
        webservicePort varchar(250),
        formElement_id bigint,
        isEditable bit not null,
        webserviceCall_id bigint not null,
        primary key (id)
    );

    alter table flow 
        add constraint UK_f2lbg87w4l8ckurvqn29yhw5u  unique (comparation_id);

    alter table images 
        add constraint UK_q68t2ifi5ad0ohm85eqk9jbu5  unique (element_id);

    alter table images 
        add constraint UK_dy9n7qskcu76kw98po10wqkgs  unique (comparation_id);

    alter table linked_form_versions 
        add constraint UK_k6vfb9l7mjmvtsnprdh4ncyh7  unique (form_id, linked_form_versions);

    alter table token 
        add constraint UK_n5llyb0plj8fyj0xra95qsjw7  unique (comparation_id);

    alter table token_between 
        add constraint UK_7393phfyfur3ypdj6lh7wcwyr  unique (id);

    alter table token_between 
        add constraint UK_f80vpw5r8wr8ob7fwvfv9db8f  unique (comparation_id);

    alter table token_comparation_answer 
        add constraint UK_1q0mna2llubobh3uuu1kmtha4  unique (id);

    alter table token_comparation_answer 
        add constraint UK_3hrlvy8i3jgq156hju7193qbo  unique (comparation_id);

    alter table token_comparation_value 
        add constraint UK_1nxjs9edjvkn1i9d14315dkni  unique (id);

    alter table token_comparation_value 
        add constraint UK_f16xon5c6ikammnr4wtau299n  unique (comparation_id);

    alter table token_in 
        add constraint UK_etg9s4o7mwaiicwejxjab260a  unique (id);

    alter table token_in 
        add constraint UK_45pd168dkiq2lm490jwcunwal  unique (comparation_id);

    alter table token_in_value 
        add constraint UK_7plyot15t7efka3kxkgkh4dlx  unique (comparation_id);

    alter table tree_answers 
        add constraint UK_dktggosx7ohcksfob4k94bfmb  unique (id);

    alter table tree_answers 
        add constraint UK_novq229qj7ibt96gyqw251biu  unique (comparation_id);

    alter table tree_blocks 
        add constraint UK_kfl0ygxd3kmomv1x5s3uy1ro4  unique (id);

    alter table tree_blocks 
        add constraint UK_lmu3cq3olf1t5gmi39cf1aac  unique (comparation_id);

    alter table tree_blocks 
        add constraint UK_5cuv65mhqmelrdm4jmpejhqiv  unique (label, version, organization_id);

    alter table tree_blocks_references 
        add constraint UK_h6705gqc3ly6jvk0gr2net9wk  unique (id);

    alter table tree_blocks_references 
        add constraint UK_nuir1gkk1iqhv8mtvj4k9vrw7  unique (comparation_id);

    alter table tree_categories 
        add constraint UK_t6nmk0eqg7yvg78lxqlrtdr66  unique (id);

    alter table tree_categories 
        add constraint UK_1xpeifv5qxo29x7yhpfmjtbyo  unique (comparation_id);

    alter table tree_dynamic_answer 
        add constraint UK_5y5wyxigj8ukynqia8jfbwe9c  unique (id);

    alter table tree_dynamic_answer 
        add constraint UK_oeg54oeq04pihalip3b3479vp  unique (comparation_id);

    alter table tree_forms 
        add constraint UK_89ul4cb5nvq3aaiyni9w6dqaq  unique (label, version, organization_id);

    alter table tree_forms 
        add constraint UK_765yjtcx0oa8unb588ngimaml  unique (id);

    alter table tree_forms 
        add constraint UK_t05hap53xy8005w0etx1tm0yx  unique (comparation_id);

    alter table tree_groups 
        add constraint UK_lkx000598o8fu6o45gc6j8j6h  unique (id);

    alter table tree_groups 
        add constraint UK_544k9wyac47tkk3mh7itf8h9f  unique (comparation_id);

    alter table tree_questions 
        add constraint UK_qmnu5ia8n490ok4d7obj7khk6  unique (id);

    alter table tree_questions 
        add constraint UK_589h63s3jthrsckwd8a4dn3xq  unique (comparation_id);

    alter table tree_system_fields 
        add constraint UK_bnumacfcvsl95y2gnfn4so2op  unique (id);

    alter table tree_system_fields 
        add constraint UK_f0b017j2vmq9p9kga9bq7o85x  unique (comparation_id);

    alter table tree_texts 
        add constraint UK_evv83frsh83pykib24ywsb5pm  unique (id);

    alter table tree_texts 
        add constraint UK_jgk76ykdcnwih8xjeno3ibtbp  unique (comparation_id);

    alter table webservice_call 
        add constraint UK_s9sdg0nbynxb17f5xenv24wov  unique (comparation_id);

    alter table webservice_call_input_link 
        add constraint UK_eyb62ax0biq5p0xlp9u3wqoo8  unique (id);

    alter table webservice_call_input_link 
        add constraint UK_t78d3ptjxutqtos6q8k1ei3nb  unique (comparation_id);

    alter table webservice_call_output_link 
        add constraint UK_t5weay02d8vl513y6nlvqp37  unique (id);

    alter table webservice_call_output_link 
        add constraint UK_a32de2wku4rhijgxgsr96ps0i  unique (comparation_id);

    alter table token 
        add constraint FK_2u42myosx0nme6wjh4jl3e0yj 
        foreign key (flow_id) 
        references flow (id);

    alter table token_between 
        add constraint FK_7tjeerxgw64y4402cmacgk1du 
        foreign key (flow_id) 
        references flow (id);

    alter table token_comparation_answer 
        add constraint FK_7uu1oap25s2nmbvsew903qq05 
        foreign key (answer_id) 
        references tree_answers (id);

    alter table token_comparation_answer 
        add constraint FK_9mrc252gn4kqsqxhs4ncvja2k 
        foreign key (flow_id) 
        references flow (id);

    alter table token_comparation_value 
        add constraint FK_mfdogudm01f0163yqe2tc480c 
        foreign key (flow_id) 
        references flow (id);

    alter table token_in 
        add constraint FK_h2dbwke556p1a2tyap957ssn0 
        foreign key (flow_id) 
        references flow (id);

    alter table token_in_value 
        add constraint FK_6ptmici4nt86dy24b0ncgmb7c 
        foreign key (answer_value) 
        references tree_answers (id);

    alter table token_in_value 
        add constraint FK_87wv2oc3y69qhprtkeb91r67d 
        foreign key (token_in) 
        references token_in (id);

    alter table tree_blocks_references 
        add constraint FK_m8991wm6hsvhvb67n6y77lrgn 
        foreign key (reference_id) 
        references tree_blocks (id);

    alter table tree_dynamic_answer 
        add constraint FK_thio6kqvdspy60mmod8x69xp7 
        foreign key (reference_id) 
        references tree_questions (id);

    alter table tree_questions 
        add constraint FK_rburksw4qm0iuf4e96pc1lcel 
        foreign key (default_value_answer) 
        references tree_answers (id);

    alter table webservice_call_input_link 
        add constraint FK_4lf5959fqm28gct2x45b4wn7k 
        foreign key (webserviceCall_id) 
        references webservice_call (id);

    alter table webservice_call_input_link_errors 
        add constraint FK_2n7xqjpvrclgb23f8gw2ax36w 
        foreign key (webserviceCallInputLink_id) 
        references webservice_call_input_link (id);

    alter table webservice_call_output_link 
        add constraint FK_7vma0jxb4jlwbucdiw4vv9net 
        foreign key (webserviceCall_id) 
        references webservice_call (id);

	CREATE TABLE `hibernate_sequence` (
		`next_val` bigint(20) DEFAULT NULL
	);

	LOCK TABLES `hibernate_sequence` WRITE;
	INSERT INTO `hibernate_sequence` VALUES (1);
	UNLOCK TABLES;


