package org.linyi.base.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.linyi.viva.db.DaoSession;
import org.linyi.viva.db.BaseNovelDbEntityDao;
import org.linyi.viva.db.HistoryRecordDbEntityDao;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/26 0026 上午 9:37
 */
@Entity
public class HistoryRecordDbEntity {

    //自增主键
    @Id(autoincrement = true)
    private Long id;
    //小说ID
    private String bookID;
    //上次更新时间
    private long time;

    private Long keyID;

    @ToOne(joinProperty = "keyID")
    private BaseNovelDbEntity bookEntity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1188821798)
    private transient HistoryRecordDbEntityDao myDao;

    public HistoryRecordDbEntity(String bookID, Long keyID, long time) {
        this.bookID = bookID;
        this.keyID = keyID;
        this.time = time;
    }

    @Generated(hash = 1882190041)
    public HistoryRecordDbEntity(Long id, String bookID, long time, Long keyID) {
        this.id = id;
        this.bookID = bookID;
        this.time = time;
        this.keyID = keyID;
    }

    @Generated(hash = 1457186099)
    public HistoryRecordDbEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookID() {
        return this.bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public Long getKeyID() {
        return this.keyID;
    }

    public void setKeyID(Long keyID) {
        this.keyID = keyID;
    }

    @Generated(hash = 1095264732)
    private transient Long bookEntity__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1341241313)
    public BaseNovelDbEntity getBookEntity() {
        Long __key = this.keyID;
        if (bookEntity__resolvedKey == null
                || !bookEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BaseNovelDbEntityDao targetDao = daoSession.getBaseNovelDbEntityDao();
            BaseNovelDbEntity bookEntityNew = targetDao.load(__key);
            synchronized (this) {
                bookEntity = bookEntityNew;
                bookEntity__resolvedKey = __key;
            }
        }
        return bookEntity;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1508912369)
    public void setBookEntity(BaseNovelDbEntity bookEntity) {
        synchronized (this) {
            this.bookEntity = bookEntity;
            keyID = bookEntity == null ? null : bookEntity.getKeyID();
            bookEntity__resolvedKey = keyID;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 856885596)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHistoryRecordDbEntityDao()
                : null;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
