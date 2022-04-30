import java.util.*;

public class GeneticAlgorithm {

    private int target;
    private int commonWeight;
    private int mutationRate;
    private double minMutateFactor;
    private double maxMutateFactor;
    private int mostCommonWeight;
    private int male;
    private int female;
    private int litterSize;
    private int maxIteration;

    private final double MALEWEIGHTRANGE = 0.3;

    private final double FEMALEWEIGHTRANGE = 0.1;

    private double largest;
    private double smallest;


    //data structures to hold mouse
    List<Rat> males;
    List<Rat> females;

    public GeneticAlgorithm(int target, int commonWeight, int mutationRate, double minMutateFactor, double maxMutateFactor,
                            int mostCommonWeight, int male, int female, int litterSize, int maxIteration) {
        this.target = target;
        this.commonWeight = commonWeight;
        this.mutationRate = mutationRate;
        this.minMutateFactor = minMutateFactor;
        this.maxMutateFactor = maxMutateFactor;
        this.mostCommonWeight = mostCommonWeight;
        this.male = male;
        this.female = female;
        this.litterSize = litterSize;
        this.maxIteration = maxIteration;

        males = new ArrayList<Rat>();
        females = new ArrayList<Rat>();
        Random random = new Random();

        largest = Double.MIN_VALUE;
        smallest = Double.MAX_VALUE;
        for (int i = 0; i < female; i++){

            //add a female mouse
            females.add(new Rat("female", random.nextDouble(commonWeight * ( 1 - FEMALEWEIGHTRANGE), commonWeight * (1 + FEMALEWEIGHTRANGE) + Double.MIN_VALUE)));
            largest = Math.max(females.get(i).getWeight(), largest);

            System.out.println("A female of weight " + females.get(i).getWeight());
        }


        for (int i = 0; i < male; i++){

            //add a male mouse
            //need to check if weight is valid
            double weight = random.nextDouble(commonWeight * ( 1 - MALEWEIGHTRANGE), commonWeight * (1 + MALEWEIGHTRANGE) + Double.MIN_VALUE);

            //keep generating
            while (weight < largest){

                weight = random.nextDouble(commonWeight * ( 1 - MALEWEIGHTRANGE), commonWeight * (1 + MALEWEIGHTRANGE) + Double.MIN_VALUE);
            }

            smallest = Math.min(smallest, weight);


            males.add(new Rat("male", weight));
            System.out.println("A male of weight " + males.get(i).getWeight());
        }

        Collections.sort(males, Collections.reverseOrder());
        Collections.sort(females, Collections.reverseOrder());



    }

    public void start(){

        int iteration = 0;

        Random random = new Random();

        List<Rat> nextMale;
        List<Rat> nextFemale;

        //check if picked index is already used
        Set<Integer> chosedMale = new HashSet<>();
        Set<Integer> chosedFemale = new HashSet<>();

        boolean reached = false;

        while (iteration < maxIteration && !reached){

            //System.out.println("Iteration " + iteration);
            //use top 10 rats
            for (int i = 0; i < 10; i++){

                //pick an index to breed
                int pickedMale = random.nextInt(0, 10);
                int pickedFemale = random.nextInt(0, 10);

                while (chosedMale.contains(pickedMale)){

                    pickedMale = random.nextInt(0, 10);
                }

                while (chosedFemale.contains(chosedFemale)){

                    pickedFemale = random.nextInt(0, 10);
                }

                for (int j = 0; j < litterSize; j++){

                    //System.out.println("Picking " + j + "th offspring");
                    Rat father = males.get(pickedMale);
                    Rat mother = females.get(pickedFemale);


                    double currentWeight = random.nextDouble(mother.getWeight(), father.getWeight() + Double.MIN_VALUE);
                    boolean willMutate = random.nextInt() % 100 < mutationRate;

                    Rat offspring = new Rat(random.nextInt() % 2 == 0 ? "male" : "female", willMutate ?
                            random.nextDouble(minMutateFactor, maxMutateFactor + Double.MIN_VALUE) * currentWeight : currentWeight);

                    if (offspring.getGender().equals("male")){

                        //not a valid male. Start over
                        if (offspring.getWeight() < largest){

                            j--;
                            continue;
                        }
                        else{

                            males.add(offspring);
                            chosedMale.add(pickedMale);
                            chosedFemale.add(pickedFemale);
                            smallest = Math.min(smallest, offspring.getWeight());
                        }
                    }
                    else{

                        //not a valid female. Start over
                        if (offspring.getWeight() > smallest){

                            j--;
                            continue;
                        }
                        else{

                            females.add(offspring);
                            chosedMale.add(pickedMale);
                            chosedFemale.add(pickedFemale);
                            largest = Math.max(largest, offspring.getWeight());
                        }

                    }

                    if (offspring.getGender() == "male" && offspring.getWeight() >= (double)target){

                        System.out.println("We produced a male rat with weight " + offspring.getWeight() + " at " + iteration + " iterations");
                        System.exit(0);
                    }

                    //System.out.println("Produced an offspring of " + offspring.getGender() + " and weight of " + offspring.getWeight());

                }

            }

            //after reproduction, we need to sort the list in descending order again.
            //also clear the hashset for holding picked index for males and females
            Collections.sort(males, Collections.reverseOrder());
            Collections.sort(females, Collections.reverseOrder());
            chosedMale.clear();
            chosedFemale.clear();
            iteration++;
        }


        System.out.println("We failed to produce " + target + "g rat in " + maxIteration + " iterations");
        System.out.println(males.get(0));

    }

    public static void main(String[] args){

        GeneticAlgorithm algorithm = new GeneticAlgorithm(50000, 300, 1, 0.5, 1.2, 300, 10, 10, 8, 1000);
        algorithm.start();

    }
}
