package com.smartdatasolutions.test.impl;

import com.smartdatasolutions.test.Member;
import com.smartdatasolutions.test.MemberExporter;
import com.smartdatasolutions.test.MemberFileConverter;
import com.smartdatasolutions.test.MemberImporter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends MemberFileConverter {

    @Override
    protected MemberExporter getMemberExporter() {
        return new MemberExporterImpl();
    }

    @Override
    protected MemberImporter getMemberImporter() {
        return new MemberImporterImpl();
    }

    @Override
    protected List<Member> getNonDuplicateMembers(List<Member> membersFromFile) {
        return membersFromFile.stream().distinct().collect(Collectors.toList());
    }

    @Override
    protected Map<String, List<Member>> splitMembersByState(List<Member> validMembers) {
        HashSet<String> states = validMembers.stream().map(Member::getState).collect(Collectors.toCollection(HashSet::new));
        Map<String, List<Member>> mapByStates = new HashMap<>();
        for (String state : states) {
            List<Member> list = new ArrayList<>();
            for (Member member : validMembers) {
                if (member.getState().equalsIgnoreCase(state)) {
                    list.add(member);
                }
            }
            mapByStates.put(state, list);
        }
        return mapByStates;
    }

    @Override
    public void convert(File inputMemberFile, String outputFilePath, String outputFileName) {
        try {
            List<Member> members = getMemberImporter().importMembers(inputMemberFile);
            List<Member> nonDuplicateMember = getNonDuplicateMembers(members);
            Map<String, List<Member>> splitByStateMembers = splitMembersByState(nonDuplicateMember);
            splitByStateMembers.forEach((key, value) -> {
                File file = new File(outputFilePath + key + outputFileName);
                try {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write("id,first name,last name,address,city,zip\n");
                    for (Member member : value) {
                        getMemberExporter().writeMember(member, writer);
                        writer.write("\n");
                    }
                    writer.close();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        File file = new File("./Members.txt");
        try {
            main.convert(file, "./", "_outputFile.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
