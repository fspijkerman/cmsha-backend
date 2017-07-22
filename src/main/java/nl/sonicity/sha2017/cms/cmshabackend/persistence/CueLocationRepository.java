package nl.sonicity.sha2017.cms.cmshabackend.persistence;

import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.CueLocation;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by htrippaers on 21/07/2017.
 */
public interface CueLocationRepository extends CrudRepository<ActiveClaim, CueLocation.CueLocationPk> {

}
