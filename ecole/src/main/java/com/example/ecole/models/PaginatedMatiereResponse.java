package com.example.ecole.models;

import java.util.List;

public class PaginatedMatiereResponse {
    private List<Matiere> matieres;
    private int totalPages;
    private long totalElements;

    public PaginatedMatiereResponse(List<Matiere> matieres, int totalPages, long totalElements) {
        this.matieres = matieres;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}

