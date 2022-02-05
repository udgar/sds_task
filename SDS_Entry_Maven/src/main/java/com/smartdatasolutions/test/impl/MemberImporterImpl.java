package com.smartdatasolutions.test.impl;

import com.smartdatasolutions.test.Member;
import com.smartdatasolutions.test.MemberImporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MemberImporterImpl implements MemberImporter {

    @Override
    public List<Member> importMembers(File inputFile) throws Exception {

        /*
         * Implement the missing logic
         */
        List<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line=br.readLine()) != null) {
                Member member = new Member();
                member.setId(line.substring(0, 9).trim());
                member.setFirstName(line.substring(12, 20).trim());
                member.setLastName(line.substring(37, 43).trim());
                member.setAddress(line.substring(62, 82).trim());
                member.setCity(line.substring(92, 112).trim());
                member.setState(line.substring(112, 114).trim());
                member.setZip(line.substring(116).trim());
                members.add(member);
            }
        }

        return members;
    }


}
