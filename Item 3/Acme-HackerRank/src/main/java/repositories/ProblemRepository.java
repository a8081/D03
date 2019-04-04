
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("select a.problem from Application a where a.id=?1")
	Problem findProblemByApplication(int applicationId);

	@Query("select p from Problem p where p.position.id=?1")
	Collection<Problem> findProblemsByPosition(int positionId);

	@Query("select p from Problem p where p.company.userAccount.id=?1")
	Collection<Problem> findProblemsByCompany(int companyUAId);

}
