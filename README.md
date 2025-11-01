# APSD - Librerie 2025

Questo repository contiene una libreria di strutture dati sviluppate in Java, come parte dell'assignment per il corso di **Analisi e Progettazione di Strutture Dati (APSD)**.


## 🎯 Scopo del Progetto

L'obiettivo di questo progetto è implementare una libreria di strutture dati, sia statiche che dinamiche, per la gestione di vettori, liste, stack, code, insiemi ordinati e non ordinati di dati generici. Le interfacce definiscono i "contratti" che le implementazioni concrete delle strutture dati (come liste, code, etc.) dovranno rispettare.


## 🏗️ Struttura delle Librerie

Le librerie sono organizzate gerarchicamente per separare i diversi livelli di astrazione:

- **`interfaces.traits`**: Definisce "tratti" o capacità di base che possono essere condivise da diverse classi, come `Reference` e `MutableReference`.
- **`interfaces.containers.base`**: Contiene le interfacce fondamentali per tutti i contenitori, come `Container`, `InsertableContainer`, `RemovableContainer`, e `ClearableContainer`.
- **`interfaces.containers.sequences`**: Specializza i contenitori per strutture dati sequenziali, definendo operazioni basate sulla posizione come `InsertableAtSequence` e `RemovableAtSequence`.


## 🧪 Fase di Testing

Per testare le implementazioni delle strutture dati, è stato fornito dal docente un 'simpletest' di base ready-to-use. È data la possibilità di integrare ultreriori test personalizzati.


## 💻 Utilizzo della Powershell

Il progetto è stato pensato per essere sviluppato e testato su ambienti Linux. Nonostante ciò, il docente ha fornito uno script utilizzabile tramite Powershell per rendere il 'makefile' runnable anche su sistemi operativi Windows, con tutte le relative funzioni implementate e funzionanti. Ambienti di sviluppo come vscode permettono l'utilizzo della Powershell tramite terminale interno.


## 👨‍💻 Autore

*   **Nominativo:** Antonio Andrea Montella
*   **Matricola:** N86005652
*   **Ateneo:** Università degli Studi di Napoli Federico II
