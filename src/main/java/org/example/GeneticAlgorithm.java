package org.example;

import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    protected static int taille_population = 4;
    protected static int len_chromosome = 5;

    protected static double pc;
    protected static double pm;

    protected static int max_generations;
    protected static Random random = new Random();

    //Initalisation de la polpulation
    protected static int[] population_initaile = {18, 6, 11, 27};

    protected static int[] valeursFitness;
    protected static double fitnessMoyenne;
    protected static int fitnessMax;
    protected static int numCroisements;
    protected static int numMuatations;

    //Fonction fitness
    protected static int fitness(int x){
        return (int) (1/(-Math.pow(x, 2) + (4 * x)));
    }

    //Selection par la méthode de Roulette
    protected static int SelectionRoulette(){
        int p1 = random.nextInt(taille_population);
        int p2 = random.nextInt(taille_population);

        if(fitness(population_initaile[p1]) > fitness(population_initaile[p2])){
            return p1;
        }else {
            return p2;
        }
    }

    //Croisement uniforme
    protected static void croisement(int parent1, int parent2){
        if(random.nextDouble() < pc){
            int chromosome1 = parent1;
            int chromosome2 = parent2;

            //Pour chaque gène , choisir aléatoirement
            //Le parent qui transmet le gène
            for(int i = 0; i < len_chromosome ; i++){
                if (random.nextBoolean() ==  true){
                    //Echanger les gènes à cette position
                    int temp = chromosome1 >>> i & 1;
                    chromosome1 ^= (chromosome1 ^ chromosome2) & 1 << i;
                    chromosome2 ^= (chromosome2 ^ temp) & 1 << i;
                }
            }
            numCroisements++;
        }
    }
     //Mutation élitiste
    protected static void mutation(int[] population_initaile){
         //Trie la population par fitness décroissante
        Arrays.sort(population_initaile);

        //Ne mute que les 2 moins bons chromosomes
        for(int i = taille_population; i > taille_population -3; i--){

            for(int j =0; j <len_chromosome; j++){
                if (random.nextDouble() < pm){
                    //Inverser le bit à cette position
                    population_initaile[i] ^= 1 << j;
                    numMuatations++;
                }
            }
        }
    }

    //Génération suivante
   protected static int[] nextGeneration(){
        int[] populationNv = new int[taille_population];

        for(int i = 0; i <taille_population; i++){
            int p1 = SelectionRoulette();
            int p2 = SelectionRoulette();

            croisement(p1, p2);
            populationNv[i] = p1;
            mutation(populationNv);

        }

        return populationNv;
    }

    protected static void AlgoGen(){
        for (int i = 0; i < max_generations ; i++){
            population_initaile = nextGeneration();

            int max = fitness(population_initaile[0]);
            for(int j = 1; j <taille_population; j++){
                max = Math.max(max, fitness(population_initaile[j]));
            }
            fitnessMax = Math.max(fitnessMax , max);
            valeursFitness[i] = max;
        }
        Arrays.sort(valeursFitness);
        fitnessMoyenne = valeursFitness[max_generations / 2];
    }

    protected static void AfficherStatistiques(){
         //Temps d'excécution
        long tempsExc = System.currentTimeMillis();

        System.out.println("Temps d'excécution (ms) : " + tempsExc);

        System.out.println("Fitness par génération : ");
        for (int fitness : valeursFitness){
            System.out.print(fitness + "");
        }
        System.out.println();

        System.out.println("Fitness moyenne : " + fitnessMoyenne);
        System.out.println("Fitness maximale :" + fitnessMax);
        System.out.println("Nombre de croisments : " + numCroisements);
        System.out.println("Nombre de mutations : " + numMuatations);
    }
    protected static void scenario(double pc, double pm, int max_generations){
        AlgoGen();
        AfficherStatistiques();
    }

    public static void main(String[] args){
        scenario(0.75, 0.005, 30);
        scenario(0.75, 0.005, 50);
        scenario(0.90, 0.01, 30);
        scenario(0.90, 0.01, 50);
    }


}
