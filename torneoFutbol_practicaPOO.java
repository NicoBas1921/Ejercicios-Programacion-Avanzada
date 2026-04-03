import java.util.ArrayList;

// CLASE: Persona
// Representa a cualquier persona del torneos
class Persona {
    private String nombre;
    private int    edad;
    private String nacionalidad;

    public Persona(String nombre, int edad, String nacionalidad) {
        this.nombre       = nombre;
        this.edad         = edad;
        this.nacionalidad = nacionalidad;
    }

    public String getNombre()       { return nombre; }
    public int    getEdad()         { return edad; }
    public String getNacionalidad() { return nacionalidad; }

    public String getRol() {
        return "Persona";
    }

    public String getInfo() {
        return getRol() + " | " + nombre +
               " | Edad: " + edad +
               " | Nacionalidad: " + nacionalidad;
    }

    @Override
    public String toString() {
        return getInfo();
    }
}

// CLASE HIJA: Jugador  (hereda de Persona)
class Jugador extends Persona {
    private String posicion;
    private int    numeroCamiseta;
    private int    goles;
    private int    tarjetasAmarillas;

    // contador estático para total de jugadores en el torneo
    private static int totalJugadores = 0;

    public Jugador(String nombre, int edad, String nacionalidad,
                   String posicion, int numeroCamiseta) {
        super(nombre, edad, nacionalidad);
        this.posicion          = posicion;
        this.numeroCamiseta    = numeroCamiseta;
        this.goles             = 0;
        this.tarjetasAmarillas = 0;
        totalJugadores++;
    }

    public String getPosicion()       { return posicion; }
    public int    getNumeroCamiseta() { return numeroCamiseta; }
    public int    getGoles()          { return goles; }
    public int    getTarjetas()       { return tarjetasAmarillas; }

    public void anotarGol() {
        this.goles++;
        System.out.println("¡Gol de " + getNombre() + "!");
    }

    public void recibirTarjeta() {
        this.tarjetasAmarillas++;
        System.out.println("Tarjeta amarilla para " + getNombre() +
                           " (total: " + tarjetasAmarillas + ")");
    }

    public static int getTotalJugadores() {
        return totalJugadores;
    }

    @Override
    public String getRol() { return "Jugador"; }

    @Override
    public String getInfo() {
        return super.getInfo() +
               " | Posición: " + posicion +
               " | Camiseta: #" + numeroCamiseta +
               " | Goles: " + goles +
               " | Amarillas: " + tarjetasAmarillas;
    }
}

// CLASE HIJA: Director Técnico (hereda de Persona)
class DirectorTecnico extends Persona {
    private String esquemaTactico;
    private int    aniosExperiencia;

    public DirectorTecnico(String nombre, int edad, String nacionalidad,
                           String esquemaTactico, int aniosExperiencia) {
        super(nombre, edad, nacionalidad);
        this.esquemaTactico   = esquemaTactico;
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getEsquema()     { return esquemaTactico; }
    public int    getExperiencia() { return aniosExperiencia; }

    @Override
    public String getRol() { return "Director Técnico"; }

    @Override
    public String getInfo() {
        return super.getInfo() +
               " | Esquema: " + esquemaTactico +
               " | Experiencia: " + aniosExperiencia + " años";
    }
}

// CLASE: Equipo
// tiene un DT y una lista de jugadores
class Equipo {
    private String           nombre;
    private String           ciudad;
    private DirectorTecnico  dt;
    private ArrayList<Jugador> plantel;
    private int puntos;
    private int golesFavor;
    private int golesContra;
    private int partidosJugados;

    public Equipo(String nombre, String ciudad, DirectorTecnico dt) {
        this.nombre          = nombre;
        this.ciudad          = ciudad;
        this.dt              = dt;
        this.plantel         = new ArrayList<>();
        this.puntos          = 0;
        this.golesFavor      = 0;
        this.golesContra     = 0;
        this.partidosJugados = 0;
    }

    public String          getNombre()   { return nombre; }
    public String          getCiudad()   { return ciudad; }
    public DirectorTecnico getDT()       { return dt; }
    public int             getPuntos()   { return puntos; }
    public int             getGolesFavor(){ return golesFavor; }
    public int             getGolesContra(){ return golesContra; }
    public int             getDiferenciaGoles() { return golesFavor - golesContra; }
    public int             getPartidosJugados() { return partidosJugados; }

    public void agregarJugador(Jugador j) {
        plantel.add(j);
    }

    // Registra el resultado de un partido para este equipo
    public void registrarResultado(int golesPropios, int golesRival) {
        this.golesFavor  += golesPropios;
        this.golesContra += golesRival;
        this.partidosJugados++;
        if (golesPropios > golesRival)       puntos += 3; // 3 puntod si es victoria
        else if (golesPropios == golesRival) puntos += 1; // 1 punto si es empate
        // derrota: 0 puntos
    }

    public void mostrarPlantel() {
        System.out.println("\n── Plantel de " + nombre + " ──");
        System.out.println("   DT: " + dt.getNombre() +
                           " | Esquema: " + dt.getEsquema());
        for (Jugador j : plantel) {
            System.out.println("   #" + j.getNumeroCamiseta() +
                               " " + j.getNombre() +
                               " (" + j.getPosicion() + ")" +
                               " - Goles: " + j.getGoles());
        }
    }

    // Busca al goleador del equipo
    public Jugador getGoleador() {
        Jugador goleador = null;
        for (Jugador j : plantel) {
            if (goleador == null || j.getGoles() > goleador.getGoles()) {
                goleador = j;
            }
        }
        return goleador;
    }

    @Override
    public String toString() {
        return nombre + " | Pts: " + puntos +
               " | PJ: " + partidosJugados +
               " | GF: " + golesFavor +
               " | GC: " + golesContra +
               " | DG: " + getDiferenciaGoles();
    }
}

// CLASE: Partido
// relaciona dos equipos y registra el resultado
class Partido {
    private Equipo local;
    private Equipo visitante;
    private int    golesLocal;
    private int    golesVisitante;
    private boolean jugado;
    private String fecha;

    private static int totalPartidos = 0;

    public Partido(Equipo local, Equipo visitante, String fecha) {
        this.local      = local;
        this.visitante  = visitante;
        this.fecha      = fecha;
        this.jugado     = false;
        totalPartidos++;
    }

    public static int getTotalPartidos() { return totalPartidos; }

    // Simula el partido registrando goles y actualizando equipos
    public void jugar(int golesLocal, int golesVisitante) {
        this.golesLocal      = golesLocal;
        this.golesVisitante  = golesVisitante;
        this.jugado          = true;

        local.registrarResultado(golesLocal, golesVisitante);
        visitante.registrarResultado(golesVisitante, golesLocal);

        System.out.println("\n Partido [" + fecha + "]: " +
                           local.getNombre() + " " + golesLocal +
                           " - " + golesVisitante + " " +
                           visitante.getNombre());
        System.out.println("   Resultado: " + getResultado());
    }

    public String getResultado() {
        if (!jugado) return "No jugado";
        if (golesLocal > golesVisitante)
            return "Victoria de " + local.getNombre();
        if (golesLocal < golesVisitante)
            return "Victoria de " + visitante.getNombre();
        return "Empate";
    }
}

// CLASE: Torneo
// gestiona equipos y partidos
class Torneo {
    private String             nombre;
    private ArrayList<Equipo>  equipos;
    private ArrayList<Partido> partidos;

    public Torneo(String nombre) {
        this.nombre   = nombre;
        this.equipos  = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public void agregarEquipo(Equipo e) { equipos.add(e); }

    public Partido programarPartido(Equipo local, Equipo visitante, String fecha) {
        Partido p = new Partido(local, visitante, fecha);
        partidos.add(p);
        return p;
    }

    // muestra la tabla de posiciones ordenada por puntos
    public void mostrarTabla() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║      TABLA DE POSICIONES - " + nombre);
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s %4s %4s %4s %4s %4s ║%n",
                          "Equipo", "Pts", "PJ", "GF", "GC", "DG");
        System.out.println("╠══════════════════════════════════════════════════╣");

        // ordenamiento simple por puntos (utiliza el ordenamiento por burbuja)
        for (int i = 0; i < equipos.size() - 1; i++) {
            for (int j = 0; j < equipos.size() - i - 1; j++) {
                if (equipos.get(j).getPuntos() < equipos.get(j+1).getPuntos()) {
                    Equipo temp = equipos.get(j);
                    equipos.set(j, equipos.get(j+1));
                    equipos.set(j+1, temp);
                }
            }
        }

        int pos = 1;
        for (Equipo e : equipos) {
            System.out.printf("║ %2d. %-17s %4d %4d %4d %4d %4d ║%n",
                              pos++, e.getNombre(), e.getPuntos(),
                              e.getPartidosJugados(), e.getGolesFavor(),
                              e.getGolesContra(), e.getDiferenciaGoles());
        }
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("Total de partidos disputados: " + Partido.getTotalPartidos());
        System.out.println("Total de jugadores inscriptos: " + Jugador.getTotalJugadores());
    }

    // muestra el goleador de cada equipo (polimorfismo: usa getInfo() de Jugador)
    public void mostrarGoleadores() {
        System.out.println("\n GOLEADORES POR EQUIPO:");
        for (Equipo e : equipos) {
            Jugador g = e.getGoleador();
            if (g != null && g.getGoles() > 0) {
                System.out.println("   " + e.getNombre() +
                                   " → " + g.getNombre() +
                                   " (" + g.getGoles() + " goles)");
            }
        }
    }
}

// CLASE PRINCIPAL
public class TorneoFutbol {

    public static void main(String[] args) {

        // ── Crear torneo ──
        Torneo torneo = new Torneo("Liga Argentina 2025");

        // ── Directores Técnicos ──
        DirectorTecnico dt1 = new DirectorTecnico("Chacho Coudet", 48, "Argentina", "4-3-3", 15);
        DirectorTecnico dt2 = new DirectorTecnico("Claudio Ubeda",   42, "Argentina", "4-2-3-1", 8);
        DirectorTecnico dt3 = new DirectorTecnico("Gustavo Costas",     40, "Argentina", "3-5-2", 3);

        // ── Equipos ──
        Equipo riverPlate = new Equipo("River Plate",  "Buenos Aires", dt1);
        Equipo boca       = new Equipo("Boca Juniors", "Buenos Aires", dt2);
        Equipo racing     = new Equipo("Racing Club",  "Avellaneda",   dt3);

        // ── Jugadores River ──
        Jugador j1 = new Jugador("Franco Armani",   37, "Argentina", "Arquero",   1);
        Jugador j2 = new Jugador("German Pezzela", 34, "Argentina", "Defensa", 26);
        Jugador j3 = new Jugador("Facundo Coliido", 26, "Argentina",  "Delantero", 11);
        riverPlate.agregarJugador(j1);
        riverPlate.agregarJugador(j2);
        riverPlate.agregarJugador(j3);

        // ── Jugadores Boca ──
        Jugador j4 = new Jugador("Agustin Marchesin",  38, "Argentina", "Arquero",   1);
        Jugador j5 = new Jugador("Edinson Cavani", 37, "Uruguay",   "Delantero", 10);
        Jugador j6 = new Jugador("Kevin Zenón",    23, "Argentina", "Volante",   7);
        boca.agregarJugador(j4);
        boca.agregarJugador(j5);
        boca.agregarJugador(j6);

        // ── Jugadores Racing ──
        Jugador j7 = new Jugador("Facundo Cambeses",   28, "Argentina",     "Arquero",   25);
        Jugador j8 = new Jugador("Adrián Martínez", 28, "Argentina", "Delantero", 9);
        Jugador j9 = new Jugador("Marcos Rojo ", 36, "Argentina",  "Defensor", 6);
        racing.agregarJugador(j7);
        racing.agregarJugador(j8);
        racing.agregarJugador(j9);

        torneo.agregarEquipo(riverPlate);
        torneo.agregarEquipo(boca);
        torneo.agregarEquipo(racing);

        // ── Mostrar planteles (polimorfismo: getRol() de cada Persona) ──
        System.out.println("════════════ PLANTELES ════════════");
        riverPlate.mostrarPlantel();
        boca.mostrarPlantel();
        racing.mostrarPlantel();

        // ── Simular partidos ──
        System.out.println("\n════════════ FECHA 1 ════════════");
        Partido p1 = torneo.programarPartido(riverPlate, boca, "05/04/2025");
        p1.jugar(2, 1);
        j3.anotarGol(); j3.anotarGol();   
        j5.anotarGol();                 
        j6.recibirTarjeta();

        System.out.println("\n════════════ FECHA 2 ════════════");
        Partido p2 = torneo.programarPartido(boca, racing, "12/04/2025");
        p2.jugar(1, 1);
        j5.anotarGol();                   
        j8.anotarGol();                   

        System.out.println("\n════════════ FECHA 3 ════════════");
        Partido p3 = torneo.programarPartido(racing, riverPlate, "19/04/2025");
        p3.jugar(0, 3);
        j3.anotarGol();                   
        j2.anotarGol(); j2.anotarGol();   

        // ── Tabla de posiciones ──
        torneo.mostrarTabla();

        // ── Goleadores ──
        torneo.mostrarGoleadores();

        // ── Info de personas (polimorfismo dinámico) ──
        System.out.println("\n🎙 INFO GENERAL (polimorfismo):");
        Persona[] personas = { dt1, dt2, j3, j5 };
        for (Persona p : personas) {
            System.out.println("   " + p.getInfo());  // llama al getInfo correcto según el tipo real
        }
    }
}
