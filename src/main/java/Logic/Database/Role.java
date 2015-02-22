package Logic.Database;

/**
 * Created by Martin on 2/12/2015.
 * Administrator can access any record and modify any account.
 * Basic Doctor is the default role for doctors
 * Super Doctor is a doctor with doctor children
 * Orgnaization Head is a doctor that can modify any member of their group
 */
public enum Role {
    ADMINISTRATOR, BASIC_DOCTOR, SUPER_DOCTOR, ORGANIZATION_HEAD, PATIENT;
}
