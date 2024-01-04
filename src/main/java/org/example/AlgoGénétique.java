package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//Structure d'enregistrement pour stocker dedans toutes les parametres du scenario
record ResultatScenario( double TempsExc, List<Double> vecteurFitness, double fitnessMoyenne,
                               double fitnessMax, int nbCrossOver, int nbmutation) {}
public class AlgoGénétique {
    private static final int taillePopulation = 4;
    private static final int lenChromosome = 5;
    private static double pc;
    private static double pm;
    //private static double [] grapheTemp;

    public static void main(String[] args) {
        ResultatScenario[] results = new ResultatScenario[4];
        
        DefaultXYZDataset dataset = new DefaultXYZDataset();

        double [] pcValues = {0.75, 0.75, 0.90, 0.90}; //Valeurs de pc respectives pour les 4 scenarios
        double [] pmValues = {0.005, 0.005, 0.01, 0.01}; //Valeurs de pm respectives pour les 4 scenarios
        int [] maxGenerationValues = { 30, 50, 30, 50};// Valeurs de maxGeneration respectives pour les 4 scenarios

        for (int i= 0; i < 4; i++){
            pc = pcValues[i];
            pm = pmValues[i];
            System.out.println("\n***********************************************************************\nScénario "+ i+1 +":");
            results[i] =deroulementScenario(maxGenerationValues[i]);
        }

        double [][][] data = new double [1][pcValues.length * pmValues.length][3];

        //Creation d'1 graphique à barres JFreeChart pour les temps d'execution
        DefaultCategoryDataset TempExec = new DefaultCategoryDataset();
        for (int i = 0; i < results.length; i++){
            TempExec.addValue(results[i].TempsExc(), "Scenario " + (i+1), "");
        }
        JFreeChart chartTemps = ChartFactory.createBarChart("Comparaison entre les temps d'execution de chaque scenario",
                "Scenarios", "Temps d'execution (ms)", TempExec);

        //Creation du graphique à lignes JFreeChart pour les valeurs fitness
        XYSeriesCollection datasetFitness = new XYSeriesCollection();
        for (int i = 0; i < results.length; i++){
            XYSeries series= new XYSeries("Scenario " + (i+1));
            for (int j = 0; j< results[i].vecteurFitness().size(); j++){
                series.add(j+1, results[i].vecteurFitness().get(j));
            }
            datasetFitness.addSeries(series);
        }
        JFreeChart chartFitness = ChartFactory.createXYLineChart("Valeurs Fitness des scenarios",
                "Iterations Fitness", "Vecteur Fitness", datasetFitness);
        //Affichage du graphique dans un panel Swing
        JFrame frame = new JFrame("Graphique de performances");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanelTemps = new ChartPanel(chartTemps);
        ChartPanel chartPanelFitness = new ChartPanel(chartFitness);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanelTemps, chartPanelFitness);
        frame.add(splitPane);
        frame.pack();
        frame.setVisible(true);
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
    private static ResultatScenario deroulementScenario(int maxGeneration) {
        List<Integer> population = generatePopulation();
        List<Double> fitnessValues = null;
        double fitnessMoyenne = 0;
        double fitnessMax = 0;
        int nbCroisements = 0;
        int nbMutations = 0;
        long tempDebut = System.currentTimeMillis();

        System.out.println("Génération initiale :");
        afficherPopulation(population);

        for(
            int generation = 1;
            generation <=maxGeneration;generation++)

            {
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

            long tempFin = System.currentTimeMillis();
            long duration = tempFin - tempDebut;
            System.out.println("Temps d'execution : "+ duration + " millisecondes");
            return new ResultatScenario(duration, fitnessValues, fitnessMoyenne, fitnessMax, nbCroisements, nbMutations);
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

    //Créer les différents graphes
    private static void CreerGraph(double [] xData, double [] yData, String Titre, String xLabel, String yLabel){
        //création du dataset pour le graphique
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] seriesData = {xData, yData};
        dataset.addSeries("Serie de donnees", seriesData);
        //création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(Titre, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, false, false);
        //Affichage du graphique dans un panel Swing
        JFrame frame = new JFrame("Graphique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

}


