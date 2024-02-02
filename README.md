# Aeropuerto con Base de Datos

## Contenido

1. [Información General](#información-general)
2. [Funcionalidades](#funcionalidades)
3. [Instrucciones de Uso](#instrucciones-de-uso)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Requisitos del Sistema](#requisitos-del-sistema)
6. [Instalación](#instalación)
7. [Configuración](#configuración)
8. [Uso](#uso)
9. [Contribuciones](#contribuciones)
10. [Contacto](#contacto)

## Información General

Este proyecto implementa un Sistema de Resrvacion de vuelos en Java a travez de modulos utilizando interfaz grafica para la ejecucion de este programa, este sistema muestra diferentes fucniones que permiten reegistrar pasajeros y aeropuertos , crear vuelos, entre otras operaciones. A través de una interfaz principalmente podras resrvar un asiento en un vuelo que se encuentro disponible

## Funcionalidades

1. ### Pasajeros:

    - Ingresar nuevos pasajeros.
    - Mostrar una lista de pasajeros registrados.
    - Borrar pasajeros, teniendo en cuenta las restricciones de eliminación las cuales impiden eliminar a un pasajero si este esta asociado a la reserva de un vuelo, y que primero debera   aceptar eliminar la recerba que a realizado para poder eliminar la informacion del pasajero.
    - Actualizar la información de un pasajero, como su nombre, su numero de identidad, entre otros, contando con algunas restricciones las cuales impiden actualizar a un pasajero si este ha resrvados un asiento, ya que si se altera la informacion de este habran incongruencias en la aplicacion del producto.
    ### 1.1 submenu Pasajeros: 
    - reservar un asiento en un vuelo.
    - calcular el precio de una reserva realizada.
    - mostar una lista de los pasajeros que han realizado una resrerva.

2. ### Aeropuestos:

    - Ingresar nuevos Aeropuertos.
    - Mostrar una lista de aeropuertos registrados.
    - Borrar aeropuertos, teniendo en cuenta las restricciones de eliminación, las que impiden que este se elimine si se encuentra asociado en la cracion de un vuelo.
    - Actualizar la información de un aeropuerto, como su nombre y ubicacion, sin embargo se ha validado en el programa,que no se podra actalizar si este se encuentra registrado en un vuelo.

3. ### Vuelos:

    - Realizar un vuelo
    - Mostrar una lista de todas los vuelos realizadas.
    - Eliminar vuelos, teniendo en cuenta las restricciones las cuales evitan eliminar un vuelo si en este hay pasajeros reservados.
    - Actualizar un vuelo, siguiendo las restricciones que se han agregado, las cuales impiden actualizar un vuelo si este tiene pasajeros que han realizado una resrvacion.



## Requisitos del Sistema

    - Java 17.
    - Consola o entorno compatible con la ejecución de Java.
    - Base de datos MySQL.

## Instalación
1. Clona o descarga el repositorio.
2. habre tu base de datos.
3. Ejecuta el script JFrameVentana.java desde tu terminal o entorno de desarrollo.


## Instrucciones de Uso
1. Interactuar con el Menú Principal:

    Una vez que el script esté en ejecución, se presentará una ventana emergente con un principal en la parte superior, donde al seleccionar se desplegara las opciones internas que este contiene.

2. Explorar las Funcionalidades:
    Al seleccionarr  la opcion que desee ejecutar y se abrira una ventana emergente segundaria para realizar la operacion correspondiente a su seleccion. Utiliza las herramientas para trabajar con el programa.


3. Salir del Programa:

    Una vez quieras finalizar la ejecucion del programa,puedes cerrar la ventana principal, parar la ejecucion desde tu editor e icluso seleccior el boton para salir que se encuentra en el menu de pasajeros.

## Estructura del Proyecto

El proyecto se organiza en módulos que abordan distintas áreas del sistema, como pasajeros, reservas, aeropuertos, vuelos . Cada módulo contiene funciones específicas para realizar las operaciones relacionadas con su área correspondiente.


## Configuración

No se requiere configuración adicional para el funcionamiento básico. Sin embargo, se pueden realizar ajustes en el código según las necesidades específicas del usuario.

## Uso

Una vez se decida utilizar el programa, siga las opciones proporcionadas, para asi realizar las distintas operaciones que el programa te ofrece ingresando la opcion segun el menu presentado.

## Contribuciones

Puedes contribuir en este codigo poniendo a prueba sus validaciones y restricciones, creando una copia podras implementar los cambios que necesites, para adaptarlo a tus necesidades.


## Contacto

Para preguntas o comentarios, puedes contactarme a través de [correo electrónico](alanisdeavilat@gmail.com).
