rm -rf out
rm -f data/transformed_products.csv

echo "=== Compiling ETLPipeline ==="
javac -d out src/org/howard/edu/lsp/assignment2/ETLPipeline.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "=== Running ETLPipeline ==="
    java -cp out org.howard.edu.lsp.assignment2.ETLPipeline
else
    echo "❌ Compilation failed!"
    exit 1
fi
