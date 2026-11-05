package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Modelo que representa un producto del inventario.
 */
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idProducto;
    private String codigo;
    private String nombre;
    private String categoria;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private int stock;
    private int stockMinimo;
    private LocalDate fechaVencimiento;
    private int estado; // 1 = Activo, 0 = Inactivo
    
    public Producto() {
        this.precioCompra = BigDecimal.ZERO;
        this.precioVenta = BigDecimal.ZERO;
        this.stock = 0;
        this.stockMinimo = 5;
        this.estado = 1;
    }
    
    public Producto(int idProducto, String codigo, String nombre, String categoria,
                    BigDecimal precioCompra, BigDecimal precioVenta, int stock,
                    int stockMinimo, LocalDate fechaVencimiento, int estado) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }
    
    public int getIdProducto() {
        return idProducto;
    }
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getPrecioCompra() {
        return precioCompra != null ? precioCompra : BigDecimal.ZERO;
    }
    
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta != null ? precioVenta : BigDecimal.ZERO;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public int getEstado() {
        return estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    /**
     * Calcula la ganancia unitaria: precioVenta - precioCompra
     */
    public BigDecimal calcularGanancia() {
        return getPrecioVenta().subtract(getPrecioCompra());
    }
    
    /**
     * Calcula el margen de ganancia porcentual: ((venta - compra) / compra) * 100
     */
    public BigDecimal calcularMargenGanancia() {
        if (getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return calcularGanancia()
                .divide(getPrecioCompra(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Indica si el stock actual es menor o igual al stock mínimo
     */
    public boolean esBajoStock() {
        return this.stock <= this.stockMinimo;
    }
    
    /**
     * Indica si el producto se encuentra vencido a la fecha actual
     */
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }
    
    public boolean isActivo() {
        return this.estado == 1;
    }
}
