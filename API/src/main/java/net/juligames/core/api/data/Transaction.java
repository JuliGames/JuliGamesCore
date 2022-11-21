package net.juligames.core.api.data;

import de.bentzin.tools.Workless;
import net.juligames.core.api.TODO;

/**
 * @author Ture Bentzin
 * 16.11.2022
 */
@Workless
@TODO(doNotcall = true)
public interface Transaction {

    void executeTransaction();

}
