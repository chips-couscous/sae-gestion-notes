
# sae-developpement-application

# Gestion de Notes - Application Java

Ce projet est une application Java appelée "Gestion de Notes", conçue pour aider les étudiants en BUT Informatique à stocker et gérer leurs notes tout au long du semestre de manière simple et personnalisable.

## Table des matières

- [Présentation du Projet](#présentation-du-projet)
- [Cahier des Charges](#cahier-des-charges)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Communication entre Deux Ordinateurs](#communication-entre-deux-ordinateurs)
- [Autres Spécifications](#autres-spécifications)
- [Contraintes Techniques](#contraintes-techniques)

## Présentation du Projet

Ce projet a été développé dans le cadre du cours de BUT Informatique et vise à simplifier la gestion des notes des étudiants. L'application permet d'importer des paramètres conformes au programme national, de saisir des notes, de calculer des moyennes, et même de partager les paramétrages entre étudiants.

## Cahier des Charges

Le cahier des charges décrit en détail les fonctionnalités de l'application, notamment l'importation de paramètres, la réinitialisation, le paramétrage des modalités d'évaluation, la saisie de notes, et la communication entre deux ordinateurs.

## Installation

Pour utiliser l'application, suivez ces étapes :

1. Clonez ce référentiel sur votre ordinateur.
2. Assurez-vous que vous avez Java installé.
3. Exécutez l'application en lançant le fichier `GestionDeNotes.jar`.

## Utilisation

Une fois l'application lancée, vous pouvez :

- Importer les paramètres du programme national.
- Réinitialiser l'application à la fin du semestre.
- Paramétrer les modalités d'évaluation.
- Saisir et calculer des notes.
- Consulter les notes et moyennes calculées.

## Communication entre Deux Ordinateurs

Cette fonctionnalité permet aux étudiants de partager les paramétrages de l'application de manière sécurisée. Assurez-vous que vous et l'autre utilisateur êtes connectés à un réseau et que vous avez leurs informations IP.

## Autres Spécifications

- Une notice d'utilisation détaillée est fournie pour l'importation des paramètres.
- Les messages de l'application sont personnalisables via les paramètres.
- L'application est livrée avec un paramétrage initial.
- Les données sont sauvegardées grâce à la sérialisation.
- L'interface graphique est conçue de manière ergonomique.

## Contraintes Techniques

- L'application utilise des sockets Java en mode connecté pour la communication entre deux ordinateurs.
- La connexion se termine automatiquement à la fin de l'échange.

N'hésitez pas à explorer le code source de l'application pour en savoir plus sur son fonctionnement interne.

## Auteurs

- Izard Thomas
- Jammes Tom
- Lapeyre Tony
- Lemaire Thomas
- Nguyen Constant
