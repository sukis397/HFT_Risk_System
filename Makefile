CXX      = g++
CXXFLAGS = -std=c++17 -O2 -Wall -Wextra -fopenmp
TARGET   = kernel_montecarlo.exe
SRC      = modulo_imperativo/main.cpp

all: $(TARGET)

$(TARGET): $(SRC)
	$(CXX) $(CXXFLAGS) -o $@ $^

clean:
	rm -f $(TARGET)

.PHONY: all cleany