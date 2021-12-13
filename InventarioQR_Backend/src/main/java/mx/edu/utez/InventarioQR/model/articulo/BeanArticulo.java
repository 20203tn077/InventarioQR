package mx.edu.utez.InventarioQR.model.articulo;

import mx.edu.utez.InventarioQR.model.categoria.BeanCategoria;

public class BeanArticulo {
    private long  codigo;
    private String nombre;
    private String descripcion;
    private BeanCategoria categoriaId;
    private int cantidad;

    public BeanArticulo() {
    }

    public BeanArticulo(long codigo, String nombre, String descripcion, BeanCategoria categoriaId, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.cantidad = cantidad;
    }

    public long  getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BeanCategoria getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(BeanCategoria categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
