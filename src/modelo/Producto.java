package modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase modelo que representa un producto del inventario
 * Implementa Serializable para transferencia de datos
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
    private int estado;
    
    // Constructor vacío
    public Producto() {
    }
    
    // Constructor para inserción (sin id)
    public Producto(String codigo, String nombre, String categoria, 
                   BigDecimal precioCompra, BigDecimal precioVenta, 
                   int stock, int stockMinimo, LocalDate fechaVencimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = 1;
    }
    
    // Constructor completo
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
    
    // ============ GETTERS ============
    
    public int getIdProducto() {
        return idProducto;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }
    
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }
    
    public int getStock() {
        return stock;
    }
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public int getEstado() {
        return estado;
    }
    
    // ============ SETTERS ============
    
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }
    
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    // ============ MÉTODOS DE NEGOCIO ============
    
    /**
     * Calcula la ganancia por unidad vendida
     * @return ganancia = precioVenta - precioCompra
     */
    public BigDecimal calcularGanancia() {
        return this.precioVenta.subtract(this.precioCompra);
    }
    
    /**
     * Calcula la ganancia total del stock disponible
     * @return ganancia total = ganancia * stock
     */
    public BigDecimal calcularGananciaTotal() {
        return calcularGanancia().multiply(new BigDecimal(this.stock));
    }
    
    /**
     * Calcula el porcentaje de margen de ganancia
     * @return porcentaje = (ganancia / precioCompra) * 100
     */
    public BigDecimal calcularMargenPorcentaje() {
        if (this.precioCompra.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return calcularGanancia()
                .divide(this.precioCompra, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
    
    /**
     * Verifica si el stock está por debajo del mínimo
     * @return true si stock <= stockMinimo
     */
    public boolean esBajoStock() {
        return this.stock <= this.stockMinimo;
    }
    
    /**
     * Verifica si el producto está vencido
     * @return true si fechaVencimiento es menor a hoy
     */
    public boolean estaVencido() {
        if (this.fechaVencimiento == null) {
            return false;
        }
        return this.fechaVencimiento.isBefore(LocalDate.now());
    }
    
    /**
     * Verifica si el producto está próximo a vencer (próximos 7 días)
     * @return true si vence dentro de 7 días
     */
    public boolean proximoAVencer() {
        if (this.fechaVencimiento == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = this.fechaVencimiento;
        return vencimiento.isAfter(hoy) && vencimiento.isBefore(hoy.plusDays(7));
    }
    
    /**
     * Verifica si el producto está activo
     * @return true si estado es 1
     */
    public boolean isActivo() {
        return this.estado == 1;
    }
    
    /**
     * Representación en String del objeto Producto
     */
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", stock=" + stock +
                ", stockMinimo=" + stockMinimo +
                ", estado=" + estado +
                '}';
    }
}
