package pl.eod2.managedUm;

import java.io.Serializable;

public class NamedNode <T> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String type;
    protected String name;
    protected Long id;
    protected String opis;
    protected T obiektDb;

    public NamedNode(T obiektDb) {
        this.obiektDb=obiektDb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public T getObiektDb() {
        return obiektDb;
    }

    public void setObiektDb(T obiektDb) {
        this.obiektDb = obiektDb;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
