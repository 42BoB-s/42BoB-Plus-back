-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- user Table Create SQL
CREATE TABLE user
(
    `userid`   VARCHAR(45)    NOT NULL    COMMENT '유저 이름',
    `role`     CHAR(1)        NOT NULL    COMMENT '관리자',
    `profile`  TEXT           NOT NULL    COMMENT '사진',
    CONSTRAINT PK_user PRIMARY KEY (userid)
);


-- menu Table Create SQL
CREATE TABLE menu
(
    `id`        INT            NOT NULL    AUTO_INCREMENT COMMENT '메뉴 인덱스',
    `menuname`  VARCHAR(45)    NOT NULL    COMMENT '메뉴 이름',
    CONSTRAINT PK_menu PRIMARY KEY (id)
);


-- room Table Create SQL
CREATE TABLE room
(
    `id`        INT            NOT NULL    AUTO_INCREMENT COMMENT '인덱스',
    `title`     VARCHAR(45)    NOT NULL    COMMENT '방 제목',
    `meetTime`  TIMESTAMP      NOT NULL,
    `location`  VARCHAR(45)    NOT NULL,
    `capacity`  INT            NOT NULL,
    `owner`     VARCHAR(45)    NOT NULL,
    `status`    VARCHAR(45)    NOT NULL,
    CONSTRAINT PK_room PRIMARY KEY (id, owner)
);

ALTER TABLE room
    ADD CONSTRAINT FK_room_owner_user_userid FOREIGN KEY (owner)
        REFERENCES user (userid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- chatmessage Table Create SQL
CREATE TABLE chatmessage
(
    `id`           INT            NOT NULL    AUTO_INCREMENT,
    `roomid`       INT            NOT NULL,
    `writer`       VARCHAR(45)    NOT NULL,
    `message`      TEXT           NOT NULL,
    `messagetype`  VARCHAR(45)    NOT NULL,
    CONSTRAINT PK_chatmessage PRIMARY KEY (id)
);

ALTER TABLE chatmessage
    ADD CONSTRAINT FK_chatmessage_roomid_room_id FOREIGN KEY (roomid)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- roomuser Table Create SQL
CREATE TABLE roomuser
(
    `id`      INT            NOT NULL    AUTO_INCREMENT,
    `roomid`  INT            NOT NULL,
    `userid`  VARCHAR(45)    NOT NULL,
    CONSTRAINT PK_roomuser PRIMARY KEY (id)
);

ALTER TABLE roomuser
    ADD CONSTRAINT FK_roomuser_roomid_room_id FOREIGN KEY (roomid)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE roomuser
    ADD CONSTRAINT FK_roomuser_userid_user_userid FOREIGN KEY (userid)
        REFERENCES user (userid) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- roommenu Table Create SQL
CREATE TABLE roommenu
(
    `id`      INT    NOT NULL    AUTO_INCREMENT,
    `roomid`  INT    NOT NULL,
    `menuid`  INT    NOT NULL,
    CONSTRAINT PK_roommenu PRIMARY KEY (id)
);

ALTER TABLE roommenu COMMENT '룸 아이디와 메뉴 매핑';

ALTER TABLE roommenu
    ADD CONSTRAINT FK_roommenu_roomid_room_id FOREIGN KEY (roomid)
        REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE roommenu
    ADD CONSTRAINT FK_roommenu_menuid_menu_id FOREIGN KEY (menuid)
        REFERENCES menu (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- banLIst Table Create SQL
CREATE TABLE banLIst
(
    `banuserid`    VARCHAR(45)    NOT NULL    COMMENT '벤한 유저',
    `baneduserid`  VARCHAR(45)    NOT NULL    COMMENT '벤 당한 유저',
    `id`           INT            NOT NULL    AUTO_INCREMENT COMMENT '인덱스',
    CONSTRAINT PK_banLIst PRIMARY KEY (id)
);

ALTER TABLE banLIst
    ADD CONSTRAINT FK_banLIst_banuserid_user_userid FOREIGN KEY (banuserid)
        REFERENCES user (userid) ON DELETE RESTRICT ON UPDATE RESTRICT;