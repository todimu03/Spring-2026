package org.howard.edu.lsp.assignment2;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ETLPipeline {
    public static void main(String[] args) throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("data/products.csv"));
        } catch (NoSuchFileException e) {
            System.err.println("Error: data/products.csv not found, program missing an input file and will terminate.");
            return;
        }

        List<String> output = new ArrayList<>();
        int rowsProcessed = 0;
        int rowsSkipped = 0;

        String header = lines.get(0);
        output.add(header + ",Price Range");

        for(int row = 1; row < lines.size(); row++) {
            String line = lines.get(row);
            String[] fields = line.split(",");

            if (fields.length != 4) {
                rowsProcessed++;
                rowsSkipped++;
                continue;
            }

            String productId = fields[0];
            try {
                Integer.parseInt(productId.trim());
            } catch(NumberFormatException e) {
                rowsProcessed++;
                rowsSkipped++;
                continue;
            }

            String productName = fields[1];
            String price = fields[2];

            double priceDecimal;
            try {
                priceDecimal = Double.parseDouble(price.trim());
            } catch (NumberFormatException e) {
                rowsProcessed++;
                rowsSkipped++;
                continue;
            }

            String category = fields[3].trim();

            if (productId.isEmpty() || productName.isEmpty() || price.isEmpty() || category.isEmpty()) {
                rowsProcessed++;
                rowsSkipped++;
                continue;
            }

            productName = productName.trim().toUpperCase();

            if(category.equals("Electronics")) {
                priceDecimal = priceDecimal * 0.9;
            }

            double priceRounded = Math.round(priceDecimal * 100.0) / 100.0;

            if(priceRounded > 500.00 && category.equals("Electronics")) {
                category = "Premium Electronics";
            }

            price = String.format("%.2f", priceRounded);

            // FIXED: Initialize priceRange
            String priceRange;
            if(priceRounded <= 10.00) {
                priceRange = "Low";
            } else if(priceRounded <= 100.00) {
                priceRange = "Medium";
            } else if (priceRounded <= 500.00) {
                priceRange = "High";
            } else {
                priceRange = "Premium";
            }

            rowsProcessed++;

            String newLine = String.join(",",
                productId,
                productName,
                price,
                category,
                priceRange
            );
            output.add(newLine);
        }

        Files.write(Paths.get("data/transformed_products.csv"), output);
        System.out.println("Count of total rows processed: " + rowsProcessed);
        System.out.println("Count of rows successfully transformed: " + (rowsProcessed - rowsSkipped));
        System.out.println("Count of rows skipped due to formatting errors: " + rowsSkipped);
        System.out.println("Output written to: " + Paths.get("data/transformed_products.csv").toAbsolutePath());
    }
}