# Pi Calculation Project

## Project Overview
This project aims to calculate the value of pi (π) using the Chudnovsky algorithm in both single-threaded and multi-threaded modes. It also measures and visualizes the time and memory performance of these two calculation methods. The project is written in Java and uses Maven for dependency management.

## Features
1. **Single-threaded and Multi-threaded Calculation**: Offers both single-threaded and multi-threaded implementations, allowing users to choose according to their needs.
2. **Performance Measurement**: Measures the time and memory usage of single-threaded and multi-threaded calculations at different precisions.
3. **Data Visualization**: Uses the JFreeChart library to present the time and memory performance data in chart form for easy comparison and analysis.

## Project Structure
```
testMaven/
├── pom.xml
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── PiCalculatorMultithreaded.java
│ │ │ ├── PiCalculatorSinglethreaded.java
│ │ │ ├── TimeMeasurement.java
│ │ │ ├── MemoryMeasurement.java
│ │ │ └── PiPerformanceVisualizer.java
│ │ └── resources/
│ │ └── examples.txt
│ └── test/
│ └── java/
│ └── Test1.java
└── target/
├── classes/
│ ├── examples.txt
│ └── META-INF/
│ └── maven/
│ └── ZheHu/
│ └── demo01/
│ ├── pom.properties
│ └── pom.xml
```

## Dependencies
The project uses the following dependencies:
- **JFreeChart**: Version 1.5.3, used for data visualization.
- **JCommon**: Version 1.0.24, a basic library for JFreeChart.

These dependencies are managed in the {insert\_element\_0\_YHBvbS54bWxg} file. Maven will automatically download and configure these dependencies.

## Steps to Run
### 1. Clone the Project
```bash
git clone https://github.com/your-repo-url/testMaven.git
cd testMaven
```

### 2. Compile the Project
Make sure you have installed Maven. Then, run the following command in the project root directory to compile the project:
```bash
mvn clean compile
```

### 3. Run the Calculation Program
**Calculate Pi (Single-threaded)**
```bash
mvn exec:java -Dexec.mainClass="PiCalculatorSinglethreaded" -Dexec.args="100"
```
The above command will calculate pi with 100 digits of precision in single-threaded mode.

**Calculate Pi (Multi-threaded)**
```bash
mvn exec:java -Dexec.mainClass="PiCalculatorMultithreaded" -Dexec.args="100"
```
The above command will calculate pi with 100 digits of precision in multi-threaded mode.

### 4. Performance Measurement
```bash
mvn exec:java -Dexec.mainClass="TimeMeasurement"
```
This command will measure the time taken by single-threaded and multi-threaded calculations at different precisions.

**Memory Measurement**
```bash
mvn exec:java -Dexec.mainClass="MemoryMeasurement"
```
This command will measure the memory usage of single-threaded and multi-threaded calculations at different precisions.

### 5. Data Visualization
```bash
mvn exec:java -Dexec.mainClass="PiPerformanceVisualizer"
```
This command will generate visual charts for time and memory performance.

### Example Data
The `src/main/resources/examples.txt` file contains example data of time and memory usage for single-threaded and multi-threaded calculations at different precisions.

### Notes
When running the PiPerformanceVisualizer class, make sure your environment supports a graphical interface, as this class will pop up a window to display the charts.
The performance of multi-threaded calculations may be affected by the system's hardware configuration.

### Contribution
If you have any improvement suggestions or find any issues, please feel free to submit an Issue or a Pull Request.

### License
This project is licensed under the MIT License.
