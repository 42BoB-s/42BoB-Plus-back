-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- user Table Create SQL
CREATE TABLE user
(
    `id`          VARCHAR(45)    NOT NULL    COMMENT '유저 이름',
    `role`        VARCHAR(45)    NOT NULL    COMMENT '관리자',
    `profile`     TEXT           NOT NULL    COMMENT '사진',
    `created_at`  TIMESTAMP      NOT NULL,
    `updated_at`  TIMESTAMP      NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (id)
);


-- menu Table Create SQL
CREATE TABLE menu
(
    `id`          BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '메뉴 인덱스',
    `name`        VARCHAR(45)    NOT NULL    COMMENT '메뉴 이름',
    `created_at`  TIMESTAMP      NOT NULL,
    `updated_at`  TIMESTAMP      NOT NULL,
    CONSTRAINT PK_menu PRIMARY KEY (id)
);


-- room Table Create SQL
CREATE TABLE room
(
    `id`            BIGINT         NOT NULL    AUTO_INCREMENT COMMENT '인덱스',
    `title`         VARCHAR(45)    NOT NULL    COMMENT '방 제목',
    `meet_time`     TIMESTAMP      NOT NULL,
    `location`      VARCHAR(45)    NOT NULL,
    `capacity`      INT            NOT NULL,
    `owner`         VARCHAR(45)    NOT NULL,
    `status`        VARCHAR(45)    NOT NULL,
    `announcement`  TEXT           NOT NULL    COMMENT '공지사항',
    `created_at`    TIMESTAMP      NOT NULL,
    `updated_at`    TIMESTAMP      NOT NULL,
    CONSTRAINT PK_room PRIMARY KEY (id)
);

ALTER TABLE room
    ADD CONSTRAINT FK_room_owner_user_id FOREIGN KEY (owner)
        REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- chatmessage Table Create SQL
CREATE TABLE chatmessage
(
    `id`            BIGINT         NOT NULL    AUTO_INCREMENT,
    `room_id`       BIGINT         NOT NULL,
    `writer`        VARCHAR(45)    NOT NULL,
    `message`       TEXT           NOT NULL,
    `message_type`  VARCHAR(45)    NOT NULL,
    `time`          TIMESTAMP      NOT NULL,
    `created_at`    TIMESTAMP      NOT NULL,
    `updated_at`    TIMESTAMP      NOT NULL,
    CONSTRAINT PK_chatmessage PRIMARY KEY (id)
);

ALTER TABLE chatmessage
    ADD CONSTRAINT FK_chatmessage_room_id_room_id FOREIGN KEY (room_id)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- participant Table Create SQL
CREATE TABLE participant
(
    `id`          BIGINT         NOT NULL    AUTO_INCREMENT,
    `room_id`     BIGINT         NOT NULL,
    `user_id`     VARCHAR(45)    NOT NULL,
    `created_at`  TIMESTAMP      NOT NULL,
    `updated_at`  TIMESTAMP      NOT NULL,
    CONSTRAINT PK_roomuser PRIMARY KEY (id)
);

ALTER TABLE participant
    ADD CONSTRAINT FK_participant_room_id_room_id FOREIGN KEY (room_id)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE participant
    ADD CONSTRAINT FK_participant_user_id_user_id FOREIGN KEY (user_id)
        REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- room_menu Table Create SQL
CREATE TABLE room_menu
(
    `id`          BIGINT       NOT NULL    AUTO_INCREMENT,
    `room_id`     BIGINT       NOT NULL,
    `menu_id`     BIGINT       NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL,
    `updated_at`  TIMESTAMP    NOT NULL,
    CONSTRAINT PK_roommenu PRIMARY KEY (id)
);

ALTER TABLE room_menu COMMENT '룸 아이디와 메뉴 매핑';

ALTER TABLE room_menu
    ADD CONSTRAINT FK_room_menu_room_id_room_id FOREIGN KEY (room_id)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE room_menu
    ADD CONSTRAINT FK_room_menu_menu_id_menu_id FOREIGN KEY (menu_id)
        REFERENCES menu (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- ban Table Create SQL
CREATE TABLE ban
(
    `id`          BIGINT         NOT NULL    AUTO_INCREMENT,
    `src`         VARCHAR(45)    NOT NULL,
    `dest`        VARCHAR(45)    NOT NULL,
    `created_at`  TIMESTAMP      NOT NULL,
    `updated_at`  TIMESTAMP      NOT NULL,
    CONSTRAINT PK_banLIst PRIMARY KEY (id)
);

ALTER TABLE ban
    ADD CONSTRAINT FK_ban_src_user_id FOREIGN KEY (src)
        REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ban
    ADD CONSTRAINT FK_ban_dest_user_id FOREIGN KEY (dest)
        REFERENCES user (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

