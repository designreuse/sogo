package com.yihexinda.auth.domain;

import com.yihexinda.core.abst.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority extends AbstractEntity {
    private static final long serialVersionUID = 9012832680693619057L;
    Integer id;
    String name;
}
