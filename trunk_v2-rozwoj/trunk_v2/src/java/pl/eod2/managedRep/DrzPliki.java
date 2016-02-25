/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRep;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author arti01
 */
public class DrzPliki {

    public class DrzPlikiPlik {

        private String nazwa;
        private String fullPatch;

        public DrzPlikiPlik(String nazwa, String fullPatch) {
            this.nazwa = nazwa;
            this.fullPatch = fullPatch;
        }

        public String getNazwa() {
            return nazwa;
        }

        public void setNazwa(String nazwa) {
            this.nazwa = nazwa;
        }

        public String getFullPatch() {
            return fullPatch;
        }

        public void setFullPatch(String fullPatch) {
            this.fullPatch = fullPatch;
        }

    }

    private static final Function<String, DrzPliki> FACTORY = new Function<String, DrzPliki>() {
        @Override
        public DrzPliki apply(String from) {
            return new DrzPliki(from.substring(0, from.length() - 1));
        }
    ;
    };
 
    private static final Function<String, String> TO_SHORT_PATH = new Function<String, String>() {

        @Override
        public String apply(String from) {
            int idx = from.lastIndexOf('/');

            if (idx < 0) {
                return from;
            }

            return from.substring(idx + 1);
        }
    ;
    };
 
    private final String path;
    private List<DrzPliki> directories;
    private List<String> files;
    private List<DrzPlikiPlik> filesDwnl;
    private final String shortPath;

    public DrzPliki(String path) {
        this.path = path;
        int idx = path.lastIndexOf('/');
        int idxWin = path.lastIndexOf('\\');
        if (idx != -1) {
            shortPath = path.substring(idx + 1);
        }else if(idxWin != -1){
            shortPath = path.substring(idxWin + 1);
        }else {
            shortPath = path;
        }
    }

    public void addDir(DrzPliki dir) {
        if (directories == null) {
            directories = Lists.newArrayList();
        }
        directories.add(dir);
    }

    public synchronized List<DrzPliki> getDirectories() {
        if (directories == null) {
            directories = Lists.newArrayList();
            //Iterables.addAll(directories, transform(filter(getResourcePaths(), containsPattern("/$")), FACTORY));

            for (File plik : new File(this.path).listFiles()) {
                if (plik.isDirectory()) {
                    directories.add(new DrzPliki(plik.getAbsolutePath()));
                }
            }
            //Iterables.addAll(directories, transform(filter(mySet, containsPattern("/$")), FACTORY));
        }
        return directories;
    }

    public synchronized List<String> getFiles() {
        if (files == null) {
            files = new ArrayList<>();
            for (File plik : new File(this.path).listFiles()) {
                if (plik.isFile()) {
                    files.add(plik.getName());

                    filesDwnl.add(new DrzPlikiPlik(plik.getName(), plik.getAbsolutePath()));
                }
            }
            //Iterables.addAll(files, transform(filter(getResourcePaths(), not(containsPattern("/$"))), TO_SHORT_PATH));
        }
        return files;
    }

    private Iterable<String> getResourcePaths() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Set<String> resourcePaths = externalContext.getResourcePaths(this.path);

        if (resourcePaths == null) {
            resourcePaths = Collections.emptySet();
        }
        System.err.println(resourcePaths);
        return resourcePaths;
    }

    public String getShortPath() {
        return shortPath;
    }

    public synchronized List<DrzPlikiPlik> getFilesDwnl() {
        if (filesDwnl == null) {
            filesDwnl = new ArrayList<>();
            for (File plik : new File(this.path).listFiles()) {
                if (plik.isFile()) {
                    filesDwnl.add(new DrzPlikiPlik(plik.getName(), plik.getAbsolutePath()));
                }
            }

        }
        return filesDwnl;
    }

    public void setFilesDwnl(List<DrzPlikiPlik> filesDwnl) {
        this.filesDwnl = filesDwnl;
    }

}
