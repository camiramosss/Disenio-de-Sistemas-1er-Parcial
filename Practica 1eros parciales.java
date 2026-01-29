// PRACTICA PARCIALES DISEÑO DE SISTEMAS (1ER PARCIAL)

//-------------------------------------MACOWINS-------------------------------------
// --- precio de venta de una prenda --> segun estado de prenda (nueva, promocion, liquidacion) + precio base
// --- tipo de prenda --> sacos, pantalones, camisas
// --- registro de ventas --> prendas vendidas, cantidad prendas vendidas, fecha de venta
// --- tipo de pago --> efectivo, tarjeta
// --- ganancias de un determinado dia


// Prendas
private class Prenda {
    private int precioBase;
    private Estado estado;
    private TipoPrenda tipoPrenda; // no hace falta hacer subclases (no tiene comportamiento distinto) --> ENUM
    private int precio() {
        return estado.precioFinal(precioBase);
    }
}

public enum TipoPrenda {
    SACO,
    PANTALON,
    CAMISA
}

interface Estado { // --> STRATEGY METHOD
    abstract Double precioFinal(Double precioBase);
}

private class Nueva implements Estado {
    public Double precioFinal(Double precioBase) {
        return precioBase; // no modifica el precioBase
    }
}

private class Promocion implements Estado {
    private int valorArestar; // valor decidido por el user
    public Double precioFinal(Double precioBase) {
        return precioBase - valorArestar;
    }
}

private class Liquidacion implements Estado {
    public Double precioFinal(Double precioBase) {
        return precioBase * 0.5; // 50% de descuento
    }
}

// Ventas
public class Venta{
    private List<Item> item; // list porque puede importar el orden (ya que tienen fecha)
    private Date fechaDeVenta;
    private TipoPago tipoPago;
    private int precioBaseVenta = items.forEach(item -> item.importe()).sum();
    private precioVenta(){
        tipoPago.precioSegunMedioDePago(precioBaseVenta)
    }
}

public class Item {
    private Prenda prenda;
    private int cantidad;

    public Double importe() {
        return prenda.precio() * cantidad;
    }
}


// para el tipo de pago se podria hacer:
// - strategy (hice)
// - template method por el metodo recargo() (la clase abstracta Venta define precioDeVenta() y usa recargo() dentro, el cual es definido por sus sublcases) !!!
// - 2 clases Venta y VentaConTarjeta (ya que no hay diferencia entre venta en efectivo y venta normal)

public class Efectivo inherits TipoPago {
    public int precioSegunMedioDePago(precioBaseVenta) {
        return precioBaseVenta
    }
}

public class Tarjeta inherits TipoPago {
    private int cantidadCuotas;
    private int coeficienteFijo;
    public int precioSegunMedioDePago(precioBaseVenta) {
        return cantidadCuotas * coeficienteFijo + precioBaseVenta * 0,01;
    }
}


// Ganancias
public class Ganancia {
    private List<Venta> ventas;
    public int gananciaDeUnDia(Date dia) { // no agrego el dia como atributo porque es solo para el metodo (se podrían agregar otros metodos como gananciaDeUnMes, gananciaDeUnAño, etc)
        return ventas.filter(venta ->venta.fechaDeVenta.equals(dia)) // no se usa el "== dia", se usa EQUALS
                     .forEach(venta -> venta.precioVenta())
                     .sum();
    }
}



//-------------------------------------QUEMEPONGO1-------------------------------------
// - Como usuario de QuéMePongo, quiero poder cargar prendas válidas para generar atuendos con ellas

// PRENDA VALIDA
// valida: que no le falte ningun atributo obligatorio
public class PrendaValida{
    private TipoPrenda tipoPrenda;
    private Material material;
    private Color colorPrincipal;
    private Color colorSecundario; // opcional --> usa otro constructor

    // contructor : expone los atributos indispensables para crear una prenda valida --> los suponemos por encima de setter
    public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.colorPrincipal = color;
  }

    // constructor sobrecargado para color secundario
    public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal, Color colorSecundario) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.colorPrincipal = colorPrincipal;
     this.colorSecundario = colorSecundario;
  }

  // para saber la categoria, se lo delega al tipo
    public Categoria categoriaDeLaPrenda() {
        return tipoPrenda.categoria();
    }
}


// TIPOS DE PRENDA Y CATEGORIAS
public abstract class tipoDePrenda {
    private Categoria categoria; // asegura que no haya tipo sin categoria
    
    public Categoria categoria() {
        return this.categoria;
    }
}

public enum Categoria{
    PARTE_SUPERIOR,
    PARTE_INFERIOR,
    CALZADO,
    ACCESORIO
}


// MATERIALES Y COLORES
public enum Material {
    ALGODON,
    LANA,
    JEAN,
    CUERO,
    SEDA
}

// el color NO DEBERIA ser un enum, ya que hay muchos colores posibles (un user puede escribir algo como amarillo patito, naranja tigre, etc)
// podemos basarnos en cierto listado como HTML, o una paleta de 8, 16 o 20 colores
// O podemos armar una paleta RGB --> da mas libertad para elegir un color
class Color{
    int rojo;
    int verde;
    int azul;
}


// VALIDACIONES
// todos los parámetros recibidos deben ser no nulos --> FAIL FAST = en el contructor se fdlla lo antes posible
    public Prenda(TipoDePrenda tipo, Material material, Color color)
        this.tipoDePrenda = requireNonNull(tipo, "tipo de prenda es obligatorio")
        this.material = requireNonNull(material, "material es obligatorio")
        this.color = requireNonNull(color, "color es obligatorio")

        // similar a hacer --> pero arriba fallamos mas rapido y no necesitamos crear una excepcion por cada atributo
        if (color == null)
        throw new FaltaColorExcepcion();



//-------------------------------------QUEMEPONGO2-------------------------------------
// - saber la TRAMA de la TELA de la prenda (lisa, rayada, cuadriculada, estampada)
// - crear prenda especificando en el siguiente orden: 1) tipo 2) material
// - guardar un borrador de la ultima prenda
// - si no se aclara la trama, se asume lisa
// - guardar solo las prendas validas


// TRAMA
// debo agregar otro atributo en la clase Prenda, para la trama
public enum Trama {
    LISA, RAYADA, CUADRICULADA, ESTAMPADA
}


// MATERIAL
// sigue siendo un enum, no puede ser una clase con atributo trama porque:
// - cada vez que creo una prenda, debo instanciar un material nuevo (si tengo miles de prendas --> tengo miles de materiales en memoria)
// - voy a tener instancias distintas para el mismo material
// - pierdo seguridad de tipos del enum --> un user podria crear ALGDN en lugar de ALGODON

// --> a veces modelar la realidad tal cual nos trae problemas técnicos
public enum Material {
    ALGODON, LANA, JEAN, CUERO, SEDA
}


// ORDEN
// ya se asegura con el constructor
// aca tambien metemos que el material como default es liso (no hay otro lugar donde meterlo)
public Prenda(TipoPrenda tipoPrenda, Material material, Color colorPrincipal, Color colorSecundario) {
     this.tipoDePrenda = tipoDePrenda;
     this.material = material;
     this.trama = Trama.LISA; // default
     this.colorPrincipal = color;
     this.colorSecundario = colorSecundario;
}


// BORRADOR
// no podemos hacer que un borrador sea una PRENDA, ya que esta clase es INMUTABLE (se completan todos sus campos o no es valida)
// no hicimos mal en hacer la prenda inmutable, solo debemos crear otra clase para el borrador
// para el BORRADOR, necesitamos una clase mutable que guarde la prenda en construccion (de a pasos) --> BUILDER PATTERN
public class Borrador{
    // es igual a la prenda, pero sus campos NO SON OBLIGATORIOS
    private TipoPrenda tipoPrenda;
    private Material material;
    private Trama trama = Trama.LISA; // default
    private Color colorPrincipal;
    private Color colorSecundario;

    // SETTERS
    // son para ir completando el borrador --> importante en builder!! hace que sus atributos no sean obligatorios
    public void setTipoPrenda(TipoPrenda tipoPrenda) {
        this.tipoPrenda = requireNonNull(tipoPrenda, "tipo de prenda es obligatorio"); // ahora sí, CUANDO SE DECIDE agregar el tipo de prenda, no se le puede pasar null
    }
    public void setMaterial(Material material) {
        this.material = requireNonNull(material, "material es obligatorio");
    }
    public void setTrama(Trama trama) {
        if (trama != null) {
            this.trama = trama; // si no me pasan trama, queda la default (lisa)
        }
    }
    public void setColorPrincipal(Color colorPrincipal) {
        this.colorPrincipal = requireNonNull(colorPrincipal, "color principal es obligatorio");
    }
    public void setColorSecundario(Color colorSecundario){
        this.colorSecundario = colorSecundario; // puede ser null, no es obligatorio
    }

    // BUILD
    // buildeamos la prenda --> validamos que todos los campos estén llenos --> guardo solo prendas VALIDAS
    // es necesario validar aca tambien, ya que el user podría nunca llamar a los setters (se podría validar sólo aca, pero los setters aseguran el fail fast)
    // PD: el NullPointerException detiene el programa y muestra el mensaje
    public Prenda buildPrenda() {
        if (this.tipoPrenda == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el tipo de prenda");
        }
        if (this.material == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el material");
        }
        if (this.trama == null) {
            
        }
        if (this.colorPrincipal == null) {
            throw new NullPointerException("No se puede crear la prenda: falta el color principal");
        }

        // pasó las validaciones --> creo la prenda
        if (this.colorSecundario != null) { // este puede ser null --> lo acomodamos a los 2 constructores de Prenda
            return new Prenda(this.tipoPrenda, this.material, this.colorPrincipal, this.colorSecundario);
        } else {
            return new Prenda(this.tipoPrenda, this.material, this.colorPrincipal);
        }
    }
}

// PD!! si solo creamos una prenda a través del borrador --> NO HACE FALTA validar nulls en la prenda como antes, el constructor queda mas simple

// USUARIO
// es quien tiene el borrador, quien lo crea y lo modifica
public class Usuario {
    private Borrador borradorActual;

    // para crear un borrador, arranco siempre con el tipo de la prenda
    public void crearNuevoBorrador(TipoPrenda tipoPrenda) {
        this.borradorActual = new Borrador();
        this.borradorActual.setTipoPrenda(tipoPrenda);
    }

    // luego para agregar otros atributos, uso los setters del borradorActual
    // en cada uno antes de pedirle al borrador que se agregue el atributo, se podría validar que exista un borradorActual --> opcional
    public void agregarMaterialAlBorrador(Material material) {
        this.borradorActual.setMaterial(material);
    }
    public void agregarTramaAlBorrador(Trama trama) {
        this.borradorActual.setTrama(trama);
    }
    public void agregarColorPrincipalAlBorrador(Color colorPrincipal) {
        this.borradorActual.setColorPrincipal(colorPrincipal);
    }
    public void agregarColorSecundarioAlBorrador(Color colorSecundario) {
        this.borradorActual.setColorSecundario(colorSecundario);
    }

    // cuando querramos crear la prenda, el borrador hace las validaciones (para que ningun campo sea null como debe ser en una prenda)
    public void crearPrenda() {
        this.borradorActual.buildPrenda();
    }
}

