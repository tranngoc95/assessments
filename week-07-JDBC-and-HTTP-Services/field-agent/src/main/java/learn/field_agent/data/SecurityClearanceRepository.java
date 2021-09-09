package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;

import java.util.List;

public interface SecurityClearanceRepository {
    SecurityClearance findById(int securityClearanceId);

    SecurityClearance findByName(String name);

    List<SecurityClearance> findAll();

    SecurityClearance add(SecurityClearance clearance);

    boolean update(SecurityClearance clearance);

    boolean safeDeleteById(int clearanceId);

    boolean fullDeleteById(int clearanceId);

    public boolean checkReferenceKey(int clearanceId);

}
