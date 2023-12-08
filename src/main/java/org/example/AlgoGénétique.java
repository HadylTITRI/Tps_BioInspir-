package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlgoGénétique {
    private static final int taillePopulation = 4;
    private static final int lenChromosome = 5;
    private static double pc;
    private static double pm;

    public static void main(String[] args) {
        // Scénario 1
        pc = 0.75;
        pm = 0.005;
        System.out.println("Scénario 1 :");
        deroulementScenario(30);

        // Scénario 2
        pc = 0.75;
        pm = 0.005;
        System.out.println("\n***********************************************************************\nScénario 2 :");
        deroulementScenario(50);

        // Scénario 3
        pc = 0.90;
        pm = 0.01;
        System.out.println("\n***********************************************************************\nScénario 3 :");
        deroulementScenario(30);

        // Scénario 4
        pc = 0.90;
        pm = 0.01;
        System.out.println("\n***********************************************************************\nScénario 4 :");
        deroulementScenario(50);
    }

    // Génération de la population initiale
    private static List<Integer> generatePopulation() {
        List<Integer> population_initiale = new ArrayList<>();
        population_initiale.add(18);
        population_initiale.add(6);
        population_initiale.add(11);
        population_initiale.add(27);
        return population_initiale;
    }

    // Evaluation de la valeur fitness d'un individu
    private static double evaluationFitness(int x) {
        double valeur = decode(x);
        return -(-valeur * valeur + 4 * valeur);
    }

    // Evaluation de la valeur fitness de chaque individu de la population
    private static List<Double> evaluation(List<Integer> population) {
        List<Double> fitnessValues = new ArrayList<>();
        for (int individu : population) {
            double fitness = evaluationFitness(individu);
            fitnessValues.add(fitness);
        }
        return fitnessValues;
    }

    // Sélection des parents en utilisant la méthode de la Roulette
    private static List<Integer> RouletteSelection(List<Integer> population, List<Double> fitnessValues) {
        List<Double> proba = new ArrayList<>();
        List<Integer> parents = new ArrayList<>();

        // Calculer les probabilités de sélection de chaque individu
        double sumFitness = 0;
        for (double fitness : fitnessValues) {
            sumFitness += fitness;
        }
        for (double fitness : fitnessValues) {
            proba.add(fitness / sumFitness);
        }

        // Sélection des parents en utilisant la méthode de la Roulette
        for (int i = 0; i < 2; i++) {
            double rand = new Random().nextDouble();
            double sumProba = 0;
            for (int j = 0; j < taillePopulation; j++) {
                sumProba += proba.get(j);
                if (sumProba > rand) {
                    parents.add(population.get(j));
                    break;
                }
            }
        }

        return parents;
    }

    // Croisement et mutation pour créer de nouveaux individus
    private static List<Integer> CroisementMutation(List<Integer> parents) {
        List<Integer> enfants = new ArrayList<>();

        // Croisement en utilisant le point de croisement aléatoire
        if (pc > new Random().nextDouble()) {
            int pointCroisement = new Random().nextInt(lenChromosome - 1) + 1;
            int parent1 = parents.get(0);
            int parent2 = parents.get(1);

            // Croisement des gènes
            int masque = (1 << pointCroisement) - 1;
            int enfant1 = (parent1 & ~masque) | (parent2 & masque);
            int enfant2 = (parent2 & ~masque) | (parent1 & masque);

            enfants.add(enfant1);
            enfants.add(enfant2);
        } else {
            enfants.addAll(parents);
        }

        // Mutation des gènes avec une probabilité pm
        for (int i = 0; i < enfants.size(); i++) {
            int enfant = enfants.get(i);
            for (int j = 0; j < lenChromosome; j++) {
                if (pm > new Random().nextDouble()) {
                    enfant ^= (1 << j);
                }
            }
            enfants.set(i, enfant);
        }

        return enfants;
    }

    // Décodage de la valeur d'un individu
    private static double decode(int x) {
        double min = 1;
        double max = 30;
        return min + (max - min) * x / ((1 << lenChromosome) - 1);
    }

    // Algorithme génétique pour résoudre le problème
    private static void deroulementScenario(int maxGeneration) {
        List<Integer> population = generatePopulation();
        List<Double> fitnessValues;
        double fitnessMoyenne;
        double fitnessMax;
        int nbCroisements = 0;
        int nbMutations = 0;

        System.out.println("Génération initiale :");
        afficherPopulation(population);

        for (int generation = 1; generation <= maxGeneration; generation++) {
            System.out.println("Génération " + generation + ":");

            fitnessValues = evaluation(population);
            fitnessMoyenne = calculerFitnessMoyenne(fitnessValues);
            fitnessMax = Collections.max(fitnessValues);

            System.out.println("Fitness moyenne : " + fitnessMoyenne);
            System.out.println("Fitness maximale : " + fitnessMax);
            System.out.println("Nombre de croisements effectués : " + nbCroisements);
            System.out.println("Nombre de mutations effectuées : " + nbMutations);

            if (generation < maxGeneration) {
                List<Integer> nouvellePopulation = new ArrayList<>();

                while (nouvellePopulation.size() < taillePopulation) {
                    List<Integer> parents = RouletteSelection(population, fitnessValues);
                    List<Integer> enfants = CroisementMutation(parents);

                    nouvellePopulation.addAll(enfants);

                    if (enfants.size() > 2) {
                        nbCroisements++;
                    }
                    if (!enfants.equals(parents)) {
                        nbMutations++;
                    }
                }

                population = nouvellePopulation;
                afficherPopulation(population);
            }
        }
    }

    // Calculer la fitness moyenne d'une population
    private static double calculerFitnessMoyenne(List<Double> fitnessValues) {
        double sumFitness = 0;
        for (double fitness : fitnessValues) {
            sumFitness += fitness;
        }
        return sumFitness / fitnessValues.size();
    }

    // Afficher la population actuelle
    private static void afficherPopulation(List<Integer> population) {
        for (int individu : population) {
            System.out.print(individu + " ");
        }
        System.out.println();
    }

}


