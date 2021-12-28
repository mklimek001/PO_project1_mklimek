import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class App extends Application{
    static int width;
    static int height;
    static int startEnergy;
    static int moveEnergy;
    static int plantEnergy;
    static float jungleRatio;
    static int startAnimalsNumber;
    static int epochNum;
    static boolean isMagic;

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();
        Button submit = new Button("Submit");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15));

        TextField mapWidth = new TextField("20");
        TextField mapHeight = new TextField("20");
        TextField startEnergy = new TextField("100");
        TextField moveEnergy = new TextField("5");
        TextField plantEnergy = new TextField("20");
        TextField jungleRatio = new TextField("0.3");
        TextField startAnimals = new TextField("10");
        TextField epochNumber = new TextField("10");
        CheckBox isMagic = new CheckBox("Magic Evolution");

        Label mapWidthLabel = new Label("Map Width");
        Label mapHeightLabel = new Label("Map Height");
        Label startEnergyLabel = new Label("Start Energy");
        Label moveEnergyLabel = new Label("Move Energy");
        Label plantEnergyLabel = new Label("Plant Energy");
        Label epochNumberLabel = new Label("Number of Epoch to proceed");
        Label jungleRatioLabel = new Label("Jungle Ratio");
        Label startAnimalsLabel = new Label("Number of Animals");
        Label problemCommunicatLabel = new Label();



        gridPane.add(mapWidthLabel, 0, 0,1,1);
        gridPane.add(mapHeightLabel, 0, 1,1,1);
        gridPane.add(startEnergyLabel, 0, 2,1,1);
        gridPane.add(moveEnergyLabel, 0, 3,1,1);
        gridPane.add(plantEnergyLabel, 0, 4,1,1);
        gridPane.add(jungleRatioLabel, 0, 5,1,1);
        gridPane.add(startAnimalsLabel, 0, 6,1,1);
        gridPane.add(mapWidth, 1, 0,1,1);
        gridPane.add(mapHeight, 1, 1,1,1);
        gridPane.add(startEnergy, 1, 2,1,1);
        gridPane.add(moveEnergy, 1, 3,1,1);
        gridPane.add(plantEnergy, 1, 4,1,1);
        gridPane.add(jungleRatio, 1, 5,1,1);
        gridPane.add(startAnimals, 1, 6,1,1);
        gridPane.add(isMagic, 0, 8,2,1);
        gridPane.add(submit, 0, 9,2,1);
        gridPane.add(problemCommunicatLabel, 0, 10,2,1);
        gridPane.add(epochNumber, 1, 7,1,1);
        gridPane.add(epochNumberLabel, 0, 7,1,1);


        submit.setOnAction(e -> saveValues(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio, startAnimals, isMagic, problemCommunicatLabel,epochNumber));

        Scene scene = new Scene(gridPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Starting data");

        primaryStage.show();
    }

    private boolean saveValues(TextField mapWidth,TextField mapHeight,TextField  startEnergy,TextField moveEnergy,TextField plantEnergy, TextField jungleRatio, TextField startAnimals, CheckBox isMagic, Label problemCom, TextField epochNum){
        try{
            int epochN = Integer.parseInt(epochNum.getText());

            int height = Integer.parseInt(mapHeight.getText());

            int width = Integer.parseInt(mapWidth.getText());

            int startEn = Integer.parseInt(startEnergy.getText());

            int moveEn = Integer.parseInt(moveEnergy.getText());

            int plantEn = Integer.parseInt(plantEnergy.getText());

            float jungleR = Float.parseFloat(jungleRatio.getText());

            int startAnim = Integer.parseInt(startAnimals.getText());

            boolean magic = isMagic.isSelected();

            if(startEn <= 0 || moveEn <= 0 || plantEn <= 0 || width <=3 || height <= 3) {
                throw new Exception("Wrong Number Given - height, width and energies have to bo greater than 0");
            }

            if(jungleR < 0 || jungleR > 1) {
                throw new Exception("Wrong Jungle Ratio");
            }

            if(startAnim < 10) {
                throw new Exception("Too Few Animals");
            }

            App.height = height;
            App.width = width;
            App.startEnergy = startEn;
            App.moveEnergy = moveEn;
            App.plantEnergy = plantEn;
            App.jungleRatio = jungleR;
            App.startAnimalsNumber = startAnim;
            App.isMagic = magic;
            App.epochNum = epochN;

            testWallMap(width,height,jungleR,startAnim);
            testRoundMap(width,height,jungleR,startAnim);
            return true;

        }catch(NumberFormatException | InterruptedException e){
            problemCom.setText("Wrong value was given!!!  " + e);
            return false;
        } catch (Exception e) {
            problemCom.setText("Problem" + e);;
            return false;
        }
    }

    void testWallMap(int rangeX, int rangeY, float jungleRatio, int animals) throws InterruptedException {
            WallWorldMap simulationWallMap = new WallWorldMap(rangeX,rangeY,jungleRatio,animals);
            ArrayList<GraphData> statisticNumOfAnimals = new ArrayList<>();
            ArrayList<GraphData> statisticNumOfGrass = new ArrayList<>();
            ArrayList<GraphData> statisticAvgNrgy = new ArrayList<>();
            ArrayList<GraphData> statisticLifeTime = new ArrayList<>();
            ArrayList<GraphData> statisticAvgChilds = new ArrayList<>();

            for(int i = 0; i < App.epochNum; i++) {
                GridPane roundedMapGrid = new GridPane();
                statisticNumOfAnimals.add(new GraphData(i, simulationWallMap.animalsList.size()));
                statisticNumOfGrass.add(new GraphData(i, simulationWallMap.grassList.size()));

                Thread.sleep(100);
                for(int j = 0; j < simulationWallMap.rangeX; j++) {
                    ColumnConstraints currColumn = new ColumnConstraints((int) Math.floor((float)500/simulationWallMap.rangeX));
                    currColumn.setHgrow(Priority.ALWAYS);
                    roundedMapGrid.getColumnConstraints().add(currColumn);
                }

                for(int j = 0; j < simulationWallMap.rangeY; j++) {
                    RowConstraints currRow = new RowConstraints((int) Math.floor((float)500/simulationWallMap.rangeY));
                    currRow.setVgrow(Priority.ALWAYS);
                    roundedMapGrid.getRowConstraints().add(currRow);
                }

                roundedMapGrid.getChildren().clear();
                int sumOfNrgy = 0;
                int sumOfChild = 0;
                int sumOfDethAge = 0;
                int numOfDeath = 0;

                for(Animal obj: simulationWallMap.animalsList){
                    sumOfNrgy += obj.energy;
                    sumOfChild += obj.numOfChilds;
                    if(obj.energy <= 0){
                        numOfDeath += 1;
                        sumOfDethAge += obj.age;
                    }

                    Label animLab = new Label("X");
                    if(obj.energy > 1.5*App.startEnergy){
                        animLab.setTextFill(Color.web("#000000"));
                    }else if(obj.energy > App.startEnergy){
                        animLab.setTextFill(Color.web("#5e0e0b"));
                    }else if(obj.energy > 0.75*App.startEnergy){
                        animLab.setTextFill(Color.web("#96211d"));
                    }else if(obj.energy > 0.5*App.startEnergy){
                        animLab.setTextFill(Color.web("#c7504c"));
                    }else if(obj.energy > 0.25*App.startEnergy){
                        animLab.setTextFill(Color.web("#e07f7b"));
                    }else{
                        animLab.setTextFill(Color.web("#f2bdbb"));
                    }


                    roundedMapGrid.add(animLab, obj.position.x, simulationWallMap.rangeY - obj.position.y,1,1);
                }

                statisticAvgNrgy.add(new GraphData(i,(double) sumOfNrgy/simulationWallMap.animalsList.size()));
                statisticAvgChilds.add(new GraphData(i,(double) sumOfChild/simulationWallMap.animalsList.size()));
                if(numOfDeath != 0){
                    statisticLifeTime.add(new GraphData(i, (double) sumOfDethAge/numOfDeath));
                }


                for(Grass obj: simulationWallMap.grassList){
                    Label grassLabel = new Label("*");
                    grassLabel.setTextFill(Color.web("#3da14a"));
                    roundedMapGrid.add(grassLabel, obj.position.x, simulationWallMap.rangeY - obj.position.y,1,1);
                }

                Stage map = new Stage();
                map.setTitle("Wall Map, Epoch: " + i);
                Scene scene = new Scene(roundedMapGrid,1550,550);
                map.setScene(scene);
                map.show();


                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Epoch");
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);

                lineChart.setTitle("Animals Statistic");

                XYChart.Series numOfAnimals = new XYChart.Series();
                numOfAnimals.setName("Number of animals in Epoch");
                for(GraphData data: statisticNumOfAnimals){
                    numOfAnimals.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
                }

                XYChart.Series numOfGrass = new XYChart.Series();
                numOfGrass.setName("Number of grass in Epoch");
                for(GraphData data: statisticNumOfGrass){
                    numOfGrass.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
                }

                XYChart.Series avgChilds= new XYChart.Series();
                avgChilds.setName("Average number of childs per animal in Epoch");
                for(GraphData data: statisticAvgChilds){
                    avgChilds.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
                }

                XYChart.Series avgDeath = new XYChart.Series();
                avgDeath.setName("Average animal death age in Epoch");
                for(GraphData data: statisticLifeTime){
                    avgDeath.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
                }

                HBox numOfAnimalsGraph = new HBox(lineChart);
                lineChart.getData().add(numOfAnimals);
                lineChart.getData().add(numOfGrass);
                lineChart.getData().add(avgChilds);
                lineChart.getData().add(avgDeath);
                roundedMapGrid.add(numOfAnimalsGraph,rangeX,0,1,rangeY);


                final NumberAxis x2Axis = new NumberAxis();
                final NumberAxis y2Axis = new NumberAxis();
                x2Axis.setLabel("Epoch");
                y2Axis.setLabel("Average energy");
                final LineChart<Number,Number> lineChartAvg =
                        new LineChart<Number,Number>(x2Axis,y2Axis);

                lineChartAvg.setTitle("Animals Avg Statistic");

                XYChart.Series avgNrgy = new XYChart.Series();
                avgNrgy.setName("Average energy of animals in Epoch");
                for(GraphData data: statisticAvgNrgy){
                    avgNrgy.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
                }

                HBox numAvgAnimalGraph = new HBox(lineChartAvg);
                lineChartAvg.getData().add(avgNrgy);
                roundedMapGrid.add(numAvgAnimalGraph,rangeX+1,0,1,rangeY);


                simulationWallMap.dethDelete();
                simulationWallMap.moving();
                simulationWallMap.feeding();
                simulationWallMap.procreation();
                simulationWallMap.growGrass();
                if(App.isMagic) simulationWallMap.magicFiveEvolution();

            }
    }



    void testRoundMap(int rangeX, int rangeY, float jungleRatio, int animals) throws InterruptedException {
        RoundedWorldMap simulationRoundMap = new RoundedWorldMap(rangeX,rangeY,jungleRatio,animals);
        ArrayList<GraphData> statisticNumOfAnimals = new ArrayList<>();
        ArrayList<GraphData> statisticNumOfGrass = new ArrayList<>();
        ArrayList<GraphData> statisticAvgNrgy = new ArrayList<>();
        ArrayList<GraphData> statisticLifeTime = new ArrayList<>();
        ArrayList<GraphData> statisticAvgChilds = new ArrayList<>();

        for(int i = 0; i < App.epochNum; i++) {
            statisticNumOfAnimals.add(new GraphData(i, simulationRoundMap.animalsList.size()));
            statisticNumOfGrass.add(new GraphData(i, simulationRoundMap.grassList.size()));
            GridPane roundedMapGrid = new GridPane();
            Thread.sleep(100);
            for(int j = 0; j < simulationRoundMap.rangeX; j++) {
                ColumnConstraints currColumn = new ColumnConstraints((int) Math.floor((float)500/simulationRoundMap.rangeX));
                currColumn.setHgrow(Priority.ALWAYS);
                roundedMapGrid.getColumnConstraints().add(currColumn);
            }

            for(int j = 0; j < simulationRoundMap.rangeY; j++) {
                RowConstraints currRow = new RowConstraints((int) Math.floor((float)500/simulationRoundMap.rangeY));
                currRow.setVgrow(Priority.ALWAYS);
                roundedMapGrid.getRowConstraints().add(currRow);
            }

            roundedMapGrid.getChildren().clear();

            int sumOfNrgy = 0;
            int sumOfChild = 0;
            int sumOfDethAge = 0;
            int numOfDeath = 0;

            for(Animal obj: simulationRoundMap.animalsList){
                sumOfNrgy += obj.energy;
                sumOfChild += obj.numOfChilds;
                if(obj.energy <= 0){
                    numOfDeath += 1;
                    sumOfDethAge += obj.age;
                }

                Label animLab = new Label("X");
                if(obj.energy > 1.5*App.startEnergy){
                    animLab.setTextFill(Color.web("#000000"));
                }else if(obj.energy > App.startEnergy){
                    animLab.setTextFill(Color.web("#5e0e0b"));
                }else if(obj.energy > 0.75*App.startEnergy){
                    animLab.setTextFill(Color.web("#96211d"));
                }else if(obj.energy > 0.5*App.startEnergy){
                    animLab.setTextFill(Color.web("#c7504c"));
                }else if(obj.energy > 0.25*App.startEnergy){
                    animLab.setTextFill(Color.web("#e07f7b"));
                }else{
                    animLab.setTextFill(Color.web("#f2bdbb"));
                }

                roundedMapGrid.add(animLab, obj.position.x, simulationRoundMap.rangeY - obj.position.y,1,1);
            }

            statisticAvgNrgy.add(new GraphData(i,(double) sumOfNrgy/simulationRoundMap.animalsList.size()));
            statisticAvgChilds.add(new GraphData(i,(double) sumOfChild/simulationRoundMap.animalsList.size()));
            if(numOfDeath != 0){
                statisticLifeTime.add(new GraphData(i, (double) sumOfDethAge/numOfDeath));
            }


            for(Grass obj: simulationRoundMap.grassList){
                Label grassLabel = new Label("*");
                grassLabel.setTextFill(Color.web("#3da14a"));
                roundedMapGrid.add(grassLabel, obj.position.x, simulationRoundMap.rangeY - obj.position.y,1,1);
            }


            Stage map = new Stage();
            map.setTitle("Round Map, Epoch: " + i);
            Scene scene = new Scene(roundedMapGrid,1550,550);
            map.setScene(scene);
            map.show();


            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Epoch");
            final LineChart<Number,Number> lineChart =
                    new LineChart<Number,Number>(xAxis,yAxis);

            lineChart.setTitle("Animals Statistic");

            XYChart.Series numOfAnimals = new XYChart.Series();
            numOfAnimals.setName("Number of animals in Epoch");
            for(GraphData data: statisticNumOfAnimals){
                numOfAnimals.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
            }

            XYChart.Series numOfGrass = new XYChart.Series();
            numOfGrass.setName("Number of grass in Epoch");
            for(GraphData data: statisticNumOfGrass){
                numOfGrass.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
            }

            XYChart.Series avgChilds= new XYChart.Series();
            avgChilds.setName("Average number of childs per animal in Epoch");
            for(GraphData data: statisticAvgChilds){
                avgChilds.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
            }

            XYChart.Series avgDeath = new XYChart.Series();
            avgDeath.setName("Average animal death age in Epoch");
            for(GraphData data: statisticLifeTime){
                avgDeath.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
            }

            HBox numOfAnimalsGraph = new HBox(lineChart);
            lineChart.getData().add(numOfAnimals);
            lineChart.getData().add(numOfGrass);
            lineChart.getData().add(avgChilds);
            lineChart.getData().add(avgDeath);
            roundedMapGrid.add(numOfAnimalsGraph,rangeX,0,1,rangeY);


            final NumberAxis x2Axis = new NumberAxis();
            final NumberAxis y2Axis = new NumberAxis();
            x2Axis.setLabel("Epoch");
            y2Axis.setLabel("Average energy");
            final LineChart<Number,Number> lineChartAvg =
                    new LineChart<Number,Number>(x2Axis,y2Axis);

            lineChartAvg.setTitle("Animals Avg Statistic");

            XYChart.Series avgNrgy = new XYChart.Series();
            avgNrgy.setName("Average energy of animals in Epoch");
            for(GraphData data: statisticAvgNrgy){
                avgNrgy.getData().add(new XYChart.Data(data.epochOnX,data.valueOnY));
            }

            HBox numAvgAnimalGraph = new HBox(lineChartAvg);
            lineChartAvg.getData().add(avgNrgy);
            roundedMapGrid.add(numAvgAnimalGraph,rangeX+1,0,1,rangeY);


            simulationRoundMap.dethDelete();
            simulationRoundMap.moving();
            simulationRoundMap.feeding();
            simulationRoundMap.procreation();
            simulationRoundMap.growGrass();
            if(isMagic) simulationRoundMap.magicFiveEvolution();

        }
    }

    void fileWriterCSV(ArrayList<Double> data) throws IOException {
        FileWriter writer = new FileWriter("C:\\CSVsaves\\Evolution.csv");

        for (int j = 0; j < data.size(); j++) {
            writer.append(String.valueOf(data.get(j)));
            writer.append("\n");
        }
        writer.close();
    }

}
