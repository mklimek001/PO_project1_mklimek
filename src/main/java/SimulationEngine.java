public class SimulationEngine implements Runnable{
    int epoch = 0;
    WallWorldMap simulationWallMap;
    RoundedWorldMap simulationRoundedMap;

    SimulationEngine(WallWorldMap simulationWallMap, RoundedWorldMap simulationRoundedMap){
        this.simulationWallMap = simulationWallMap;
        this.simulationRoundedMap = simulationRoundedMap;
    }

    public void fullProceess(){
        System.out.println("Process Started");
        simulationWallMap.dethDelete();
        simulationWallMap.moving();
        simulationWallMap.feeding();
        simulationWallMap.procreation();
        simulationWallMap.growGrass();

        simulationRoundedMap.dethDelete();
        simulationRoundedMap.moving();
        simulationRoundedMap.feeding();
        simulationRoundedMap.procreation();
        simulationRoundedMap.growGrass();

        epoch += 1;

        for(Animal obj: simulationWallMap.animalsList){
            System.out.println(obj.position +" : "+ obj.position);
        }

        for(Grass obj: simulationWallMap.grassList){
            System.out.println(obj.position +" : "+ obj.position);
        }

        for(Animal obj: simulationRoundedMap.animalsList){
            System.out.println(obj.position +" : "+ obj.position);
        }

        for(Grass obj: simulationRoundedMap.grassList){
            System.out.println(obj.position +" : "+ obj.position);
        }

    }


    @Override
    public void run() {
        String[] args = {};
        App.main(args);
        this.fullProceess();

    }
}
