package org.malagu.multitenant.strategy;

import org.malagu.multitenant.domain.Organization;

/**
 * Created by kevin on 2017/11/24.
 */
public interface CurrentOrganizationStrategy {
    Organization getCurrent();
}
