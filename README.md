# Librerie per il corso di APSD

Questo repository contiene una collezione di interfacce Java per la definizione di strutture dati, sviluppate come parte dell'assignment per il corso di **Algoritmi e Programmazione Strutturata e a Oggetti (APSD)**.

## 🎯 Scopo del Progetto

L'obiettivo di questo progetto è fornire un'architettura robusta e flessibile di interfacce per la creazione di diverse tipologie di contenitori di dati. Le interfacce definiscono i contratti che le implementazioni concrete delle strutture dati (come liste, code, pile, etc.) dovranno rispettare.

Il design segue le specifiche fornite dal docente, promuovendo principi di programmazione a oggetti come l'ereditarietà e il polimorfismo.

## 🏗️ Struttura delle Librerie

Le librerie sono organizzate gerarchicamente per separare i diversi livelli di astrazione:

- **`interfaces.traits`**: Definisce "tratti" o capacità di base che possono essere condivise da diverse classi, come `Reference` e `MutableReference`.
- **`interfaces.containers.base`**: Contiene le interfacce fondamentali per tutti i contenitori, come `Container`, `InsertableContainer`, `RemovableContainer`, e `ClearableContainer`.
- **`interfaces.containers.sequences`**: Specializza i contenitori per strutture dati sequenziali, definendo operazioni basate sulla posizione come `InsertableAtSequence` e `RemovableAtSequence`.

## 🧪 Come Testare

Il file `Main.java` contiene il punto di ingresso del programma. Per testare le implementazioni delle strutture dati che utilizzano queste interfacce, è possibile aggiungere il proprio codice di test all'interno del metodo `main`.

```java
public class Main {
  static public void main(String[] args) {
    System.out.println("APSD Libraries 2025!");

    // Aggiungere test personali qui!
  }
}
```

## 👨‍💻 Autore

*   **Nome:** Antonio Andrea Montella
*   **Matricola:** N86005652
*   **Ateneo:** Università degli Studi di Napoli Federico II