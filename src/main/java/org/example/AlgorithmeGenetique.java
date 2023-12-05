package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlgorithmeGenetique {

    private static final int taillePopulation = 4;
    private static final int lenChromosome = 5;
    private static final double MUTATION_RATE = 0.1;

    private static void deroulementScenario(double pc, double pm, int maxGenerations) {
        long startTime = System.currentTimeMillis();

        // Initialisation de la population
        List<Integer> population_initiale = new ArrayList<>();
        population_initiale.add(18);
        population_initiale.add(6);
        population_initiale.add(11);
        population_initiale.add(27);



        // Déroulement de l'algorithme génétique
        for (int iteration = 0; iteration < maxGenerations; iteration++) {

            // Evaluation de la valeur fitness de chaque in>,&3Z2zdividu de la population
            List<Double> fitnessValues = evaluation(population_initiale);


            // Séléction des parents en utilisant la méthode de la Roulette
            List<Integer> parents = RouletteSelection(population_initiale, fitnessValues);

            // Appliquer le croisement et la mutation pour créer des nouveaux individus , nouvelle population
            // Remplacer les parents par les nouveaux individus générés
            population_initiale = CroisementMutation(parents, pc, pm);

            // Afficher l'individu qui a la valeur fitness améliorée
            System.out.println("Iteration " + (iteration + 1) + ": Best Individual = " + population_initiale.get(0) +
                    ", Fitness = " + evaluationFitness(population_initiale.get(0)));
        }

        // Affichage des statistiques, résultats
        long endTime = System.currentTimeMillis();

        System.out.println("Scenario: Pc = " + pc + ", Pm = " + pm + ", Max Generations = " + maxGenerations);
        System.out.println("Temps d’exécution de l’algorithme : " + (endTime - startTime) + " milliseconds");
        System.out.println("Vecteur des Fitness : " + evaluation(population_initiale));
        System.out.println("Fitness moyenne : " + calculFitness(population_initiale));
        System.out.println("Fitness maximale : " + evaluationFitness(population_initiale.get(0)));
        System.out.println("Nombre de croisements effectués : " + ((maxGenerations / 2) * taillePopulation));
        System.out.println("Nombre de mutations effectués : " + (int) (MUTATION_RATE * maxGenerations * taillePopulation));
        System.out.println("---------------------------------------------");
    }


    //Méthode d'évaluation des individus
    private static List<Double> evaluation(List<Integer> population) {
        List<Double> valeursFitness = new ArrayList<>();

            for (Integer individu : population) {
                double x = decode(individu);
                double fitness = evaluationFitness(x);

                valeursFitness.add(fitness);
            }

        return valeursFitness;
    }

    //Méthode de séléction basée sur la méthode de la Roulette
    private static List<Integer> RouletteSelection(List<Integer> population_initiale, List<Double> valeursFitness) {
        List<Integer> parents = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < taillePopulation / 2; i++) {
            // Séléctioner deux individus d'une manière aléatoire
            int index1 = random.nextInt(taillePopulation);
            int index2 = random.nextInt(taillePopulation);

            // Séléctionner l'individu qui a la meilleur valeur fitness
            double fitness1 = valeursFitness.get(index1);
            double fitness2 = valeursFitness.get(index2);

            int parent1 = (fitness1 > fitness2) ? population_initiale.get(index1) : population_initiale.get(index2);

            // Répéter le processus pour séléctionner le deuxième individu (parent)
            index1 = random.nextInt(taillePopulation);
            index2 = random.nextInt(taillePopulation);

            double fitness3 = valeursFitness.get(index1);
            double fitness4 = valeursFitness.get(index2);

            int parent2 = (fitness3 > fitness4) ? population_initiale.get(index1) : population_initiale.get(index2);

            // Ajouter le parent séléctionné à la liste
            parents.add(parent1);
            parents.add(parent2);
        }

        return parents;
    }

    //Méthodes de croisement et mutation
    private static List<Integer> CroisementMutation(List<Integer> parents, double pc, double pm) {
        Random random = new Random();

        List<Integer> populationNv = new ArrayList<>();

        for (int i = 0; i < parents.size(); i += 2) {
            int parent1 = parents.get(i);
            int parent2 = parents.get(i + 1);

            // Application de la méthode de croisement uniforme
            if (random.nextDouble() < pc){
                int enfant1 = 0;
                int enfant2 = 0;
                for (int j = 0; j < lenChromosome ; j++){
                    if(random.nextBoolean()){
                        enfant1 |= parent1 & (1 << j);
                        enfant2 |= parent2 & (1 << j);
                    }else{
                        enfant1 |= parent2 & (1 << j);
                        enfant2 |= parent1 & (1 << j);
                    }
                }
                parent1 = enfant1;
                parent2 = enfant2;
            }

            // Application de la mutation
            if (random.nextDouble() < pm && i < taillePopulation) {
                parent1 = mutate(parent1);
            }
            if (random.nextDouble() < pm && i < taillePopulation) {
                parent2 = mutate(parent2);
            }

            // Ajouter l'enfant (nouveau individu généré ) à la popoulation
            populationNv.add(parent1);
            populationNv.add(parent2);
        }

        populationNv.addAll(parents);

        // Trier la nouvelle population en se basant sur la valeur fitness
        populationNv.sort((a, b) -> Double.compare(evaluationFitness(b), evaluationFitness(a)));

        return populationNv;
    }

    private static int mutate(int individual) {
        // Inversion d'un bit aléatoire
        Random random = new Random();

        int mutationPoint = 1 << random.nextInt(lenChromosome);
        return individual ^ mutationPoint;
    }

    private static double decode(int individual) {
        // Convertion représentation binaire -> valeur décimale correspondante
        return 1.0 + (29.0 / (Math.pow(2, lenChromosome) - 1)) * individual;
    }

    private static double evaluationFitness(double x) {
        // Fonction objective est de maximiser la fonction fitness : -x^2 + 4x
        return -(-Math.pow(x, 2) + 4 * x);
    }

    //Méthode pour calculer la valeur fitness
    private static double calculFitness(List<Integer> valeursFitness) {
        double sum = 0;
        for (Integer fitness : valeursFitness) {
            sum += fitness;
        }
        return (sum / valeursFitness.size());
    }



    public static void main(String[] args) {

        //Déroulement des scécnarios
        // Scénario 1
        deroulementScenario(0.75, 0.005, 30);

        // Scénario 2
        deroulementScenario(0.75, 0.005, 50);

        // Scénario 3
        deroulementScenario(0.90, 0.01, 30);

        // Scénario 4
        deroulementScenario(0.90, 0.01, 50);

    }

}
