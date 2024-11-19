
CREATE TABLE user (
                      uid VARCHAR(50) PRIMARY KEY,
                      uname VARCHAR(50) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(50) NOT NULL,
                      status ENUM('online', 'offline') DEFAULT 'offline',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversations (
                               cid VARCHAR(36) PRIMARY KEY,  -- 使用 CHAR(36) 存储 UUID
                               is_group BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE user_conversations (
                                    uid1 VARCHAR(50),
                                    uid2 VARCHAR(50),
                                    cid VARCHAR(36),
                                    role ENUM('member', 'admin') DEFAULT 'member',
                                    PRIMARY KEY (uid1, cid),
                                    FOREIGN KEY (uid1) REFERENCES user(uid) ON DELETE CASCADE,
                                    FOREIGN KEY (uid2) REFERENCES user(uid) ON DELETE CASCADE,
                                    FOREIGN KEY (cid) REFERENCES conversations(cid) ON DELETE CASCADE
);

CREATE TABLE messages
(
    mid       INT AUTO_INCREMENT PRIMARY KEY,
    cid       VARCHAR(36),
    sid       VARCHAR(50),
    content   TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status    ENUM('sent', 'delivered', 'read') DEFAULT 'sent',
    FOREIGN KEY (cid) REFERENCES conversations (cid) ON DELETE CASCADE,
    FOREIGN KEY (sid) REFERENCES user (uid) ON DELETE CASCADE
);

CREATE TABLE friendlist (
                            uid VARCHAR(50),
                            fid VARCHAR(50),
                            status ENUM('requested', 'accepted', 'blocked') DEFAULT 'requested',
                            PRIMARY KEY (uid, fid),
                            FOREIGN KEY (uid) REFERENCES user(uid) ON DELETE CASCADE,
                            FOREIGN KEY (fid) REFERENCES user(uid) ON DELETE CASCADE
);
