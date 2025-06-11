package com.hbs.hbsfinan.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemMaterialId implements Serializable{


        private Long doacaoMaterial;
        private int produto;

        public ItemMaterialId() {}

        public ItemMaterialId(Long doacaoMaterial, int produto) {
            this.doacaoMaterial = doacaoMaterial;
            this.produto = produto;
        }

        public Long getDoacaoMaterial() {
            return doacaoMaterial;
        }

        public void setDoacaoMaterial(Long doacaoMaterial) {
            this.doacaoMaterial = doacaoMaterial;
        }

        public int getProduto() {
            return produto;
        }

        public void setProduto(int produto) {
            this.produto = produto;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemMaterialId)) return false;
        ItemMaterialId that = (ItemMaterialId) o;
        return produto == that.produto &&
                Objects.equals(doacaoMaterial, that.doacaoMaterial);
    }


    @Override
        public int hashCode() {
            return Objects.hash(doacaoMaterial, produto);
        }
    }


